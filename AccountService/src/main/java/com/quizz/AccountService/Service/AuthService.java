package com.quizz.AccountService.Service;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.quizz.AccountService.DTO.Request.LoginRequest;
import com.quizz.AccountService.Entity.User;
import com.quizz.AccountService.Exception.AppException;
import com.quizz.AccountService.Exception.ErrorCode;
import com.quizz.AccountService.Repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class AuthService {

    @NonFinal
    @Value("${key.jwt.value}")
    String KEY;
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;

    public String login(LoginRequest request) throws JOSEException {
        User user = userRepository.findByName(request.getUsername())
                .orElseThrow(()-> new AppException(ErrorCode.USER_NO_EXIST));

        boolean check = passwordEncoder.matches(request.getPassword(), user.getPassword());

        if(!check)
            throw new AppException(ErrorCode.PASSWORD_LOGIN_FAIL);

        return generate(user);
    }

    public String findName(String token) {
        String name = null;
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            name = signedJWT.getJWTClaimsSet().getSubject();
        } catch (ParseException e) {
            throw new AppException(ErrorCode.PARSE_TOKEN_FAIL);
        }

        if(!userRepository.existsByName(name))
            throw new AppException(ErrorCode.USER_NO_EXIST);

        return name;
    }





    //Create Token
    public String generate(User user) throws JOSEException {
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .jwtID(UUID.randomUUID().toString())
                .issuer("QUIZZ")
                .subject(user.getName())
                .issueTime(Date.from(Instant.now()))
                .expirationTime(Date.from(Instant.now().plus(1,ChronoUnit.HOURS)))
                .claim("scope", buildScope(user))
                .build();

        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS256);
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(jwsHeader,payload);

        jwsObject.sign(new MACSigner(KEY.getBytes()));
        return jwsObject.serialize();
    }

    public String buildScope(User user){
        StringJoiner stringJoiner = new StringJoiner(" ");
        if (!CollectionUtils.isEmpty(user.getRoles())) {
            user.getRoles().forEach(role -> {
                stringJoiner.add(role.getName());
                if (!CollectionUtils.isEmpty(role.getPermissions())) {
                    role.getPermissions().forEach(permission -> stringJoiner.add(permission.getName()));
                }
            });
        }
        return stringJoiner.toString();
    }
}
