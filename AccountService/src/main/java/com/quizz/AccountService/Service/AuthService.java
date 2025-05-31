package com.quizz.AccountService.Service;

import com.app.CommonLibrary.Exception.AppException;
import com.app.CommonLibrary.Exception.ErrorCode;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.quizz.AccountService.DTO.Request.LoginRequest;
import com.quizz.AccountService.DTO.Request.TokenRequest;
import com.quizz.AccountService.Entity.Redis.Token;
import com.quizz.AccountService.Entity.MySql.User;
import com.quizz.AccountService.Mapper.UserMapper;
import com.quizz.AccountService.Repository.Redis.LockUserRepository;
import com.quizz.AccountService.Repository.Redis.TokenRepository;
import com.quizz.AccountService.Repository.MySql.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class AuthService {

    @NonFinal
    @Value("${key.jwt.value}")
    String KEY;

    @NonFinal
    @Value("${app.time.expiryTime}")
    int expiryTime;

    UserRepository userRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;
    TokenRepository tokenRepository;
    LockUserRepository lockUserRepository;

    public String login(LoginRequest request) throws JOSEException {
        User user = userRepository.findByName(request.getUsername())
                .orElseThrow(()-> new AppException(ErrorCode.USER_NO_EXIST));

        if(lockUserRepository.existsById(user.getUserID()))
            throw new AppException(ErrorCode.USER_LOCK);

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
            throw new AppException(ErrorCode.AUTHENTICATION);
        }

        if(!userRepository.existsByName(name))
            throw new AppException(ErrorCode.USER_NO_EXIST);

        return name;
    }

    public Boolean instropect(TokenRequest request) throws ParseException, JOSEException {
        SignedJWT jwt = SignedJWT.parse(request.getToken());
        var expiryTime = jwt.getJWTClaimsSet().getExpirationTime();
        JWSVerifier jwsVerifier = new MACVerifier(KEY.getBytes());

        boolean isVerify = jwt.verify(jwsVerifier);
        boolean isTime = expiryTime.after(Date.from(Instant.now()));
        boolean isExists = tokenRepository.existsById(jwt.getJWTClaimsSet().getJWTID());

        if(!isVerify || !isTime || !isExists)
            throw new AppException(ErrorCode.AUTHENTICATION);

        return true;
    }

    public Boolean logout(TokenRequest request) throws ParseException, JOSEException {
        instropect(request);//Exception
        SignedJWT jwt = SignedJWT.parse(request.getToken());
        tokenRepository.deleteById(jwt.getJWTClaimsSet().getJWTID());
        return true;
    }


    //Create Token
    String generate(User user) throws JOSEException {
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .jwtID(UUID.randomUUID().toString())
                .issuer("QUIZZ")
                .subject(user.getName())
                .issueTime(Date.from(Instant.now()))
                .expirationTime(Date.from(Instant.now().plus(expiryTime,ChronoUnit.SECONDS)))
                .claim("scope", buildScope(user))
                .build();

        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS256);
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(jwsHeader,payload);
        jwsObject.sign(new MACSigner(KEY.getBytes()));
        String token = jwsObject.serialize();

        tokenRepository.save(Token.builder()
                        .tokenID(jwtClaimsSet.getJWTID())
                        .value(token)
                        .expiryTime(expiryTime)
                .build());

        return token;
    }

    String buildScope(User user){
        StringJoiner stringJoiner = new StringJoiner(" ");
        stringJoiner.add(user.getRole().getName());
        if (!CollectionUtils.isEmpty(user.getRole().getPermissions())) {
            user.getRole().getPermissions().forEach(permission -> stringJoiner.add(permission.getName()));
        }

        return stringJoiner.toString();
    }
}
