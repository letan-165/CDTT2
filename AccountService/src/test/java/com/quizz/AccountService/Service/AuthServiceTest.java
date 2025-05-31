package com.quizz.AccountService.Service;

import com.app.CommonLibrary.Exception.AppException;
import com.app.CommonLibrary.Exception.ErrorCode;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.quizz.AccountService.DTO.Request.LoginRequest;
import com.quizz.AccountService.DTO.Request.TokenRequest;
import com.quizz.AccountService.Entity.MySql.Permission;
import com.quizz.AccountService.Entity.MySql.Role;
import com.quizz.AccountService.Entity.MySql.User;
import com.quizz.AccountService.Mapper.UserMapper;
import com.quizz.AccountService.Repository.MySql.UserRepository;
import com.quizz.AccountService.Repository.Redis.LockUserRepository;
import com.quizz.AccountService.Repository.Redis.TokenRepository;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthServiceTest {
    @InjectMocks
    AuthService authService;

    @Mock UserRepository userRepository;
    @Mock UserMapper userMapper;
    @Mock PasswordEncoder passwordEncoder;
    @Mock TokenRepository tokenRepository;
    @Mock LockUserRepository lockUserRepository;

    User user;

    @BeforeEach
    void initData(){
        ReflectionTestUtils.setField(authService, "KEY", "M".repeat(32));
        ReflectionTestUtils.setField(authService, "expiryTime", 3600);
        user = User.builder()
                .userID("tanID")
                .name("tan")
                .role(Role.builder()
                        .name("R1")
                        .permissions(new ArrayList<>(List.of(Permission.builder().name("P1").build(),
                                Permission.builder().name("P2").build())))
                        .build())
                .build();
    }

    @Test
    void generate_success() throws JOSEException, ParseException {
        String token = authService.generate(user);

        assertThat(token).isNotEmpty();

        SignedJWT jwt = SignedJWT.parse(token);
        JWTClaimsSet jwtClaimsSet = jwt.getJWTClaimsSet();

        verify(tokenRepository).save(any());

        assertThat(jwtClaimsSet.getJWTID()).isNotEmpty();
        assertThat(jwtClaimsSet.getIssuer()).isEqualTo("QUIZZ");
        assertThat(jwtClaimsSet.getSubject()).isEqualTo(user.getName());
        assertThat(jwtClaimsSet.getIssueTime()).isBefore(jwtClaimsSet.getExpirationTime());
        assertThat(jwtClaimsSet.getClaim("scope")).isEqualTo("R1 P1 P2");
    }

    @Test
    void login_success() throws JOSEException {
        LoginRequest request = LoginRequest.builder()
                .username("tan")
                .password("123")
                .build();

        when(userRepository.findByName(eq(request.getUsername()))).thenReturn(Optional.of(user));
        when(lockUserRepository.existsById(eq(user.getUserID()))).thenReturn(false);
        when(passwordEncoder.matches(eq(request.getPassword()), eq(user.getPassword()))).thenReturn(true);

        String response = authService.login(request);

        assertThat(response).isNotEmpty();
    }

    @Test
    void login_fail_USER_NO_EXIST() throws JOSEException {
        LoginRequest request = LoginRequest.builder()
                .username("tan")
                .password("123")
                .build();

        when(userRepository.findByName(eq(request.getUsername()))).thenReturn(Optional.empty());
        var exception = assertThrows(AppException.class,
                ()->authService.login(request));

        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.USER_NO_EXIST);
    }

    @Test
    void login_fail_USER_LOCK() throws JOSEException {
        LoginRequest request = LoginRequest.builder()
                .username("tan")
                .password("123")
                .build();

        when(userRepository.findByName(eq(request.getUsername()))).thenReturn(Optional.of(user));
        when(lockUserRepository.existsById(eq(user.getUserID()))).thenReturn(true);
        var exception = assertThrows(AppException.class,
                ()->authService.login(request));

        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.USER_LOCK);
    }

    @Test
    void login_fail_PASSWORD_LOGIN_FAIL() throws JOSEException {
        LoginRequest request = LoginRequest.builder()
                .username("tan")
                .password("123")
                .build();

        when(userRepository.findByName(eq(request.getUsername()))).thenReturn(Optional.of(user));
        when(lockUserRepository.existsById(eq(user.getUserID()))).thenReturn(false);
        when(passwordEncoder.matches(eq(request.getPassword()), eq(user.getPassword()))).thenReturn(false);

        var exception = assertThrows(AppException.class,
                ()->authService.login(request));

        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.PASSWORD_LOGIN_FAIL);
    }

    @Test
    void instropect_success() throws ParseException, JOSEException {
        try (MockedStatic<SignedJWT> mocked = mockStatic(SignedJWT.class)) {
            TokenRequest request = mock(TokenRequest.class);
            SignedJWT jwt = mock(SignedJWT.class);
            JWTClaimsSet jwtClaimsSet = mock(JWTClaimsSet.class);

            mocked.when(() -> SignedJWT.parse(request.getToken())).thenReturn(jwt);
            when(jwt.getJWTClaimsSet()).thenReturn(jwtClaimsSet);
            when(jwtClaimsSet.getExpirationTime()).thenReturn(Date.from(Instant.now().plus(1, ChronoUnit.MINUTES)));
            when(jwt.verify(any(JWSVerifier.class))).thenReturn(true);
            when(tokenRepository.existsById(any())).thenReturn(true);

            Boolean response = authService.instropect(request);

            assertThat(response).isTrue();
        }
    }

    @Test
    void instropect_fail_isVerify() throws ParseException, JOSEException {
        try (MockedStatic<SignedJWT> mocked = mockStatic(SignedJWT.class)) {
            TokenRequest request = mock(TokenRequest.class);
            SignedJWT jwt = mock(SignedJWT.class);
            JWTClaimsSet jwtClaimsSet = mock(JWTClaimsSet.class);

            mocked.when(() -> SignedJWT.parse(request.getToken())).thenReturn(jwt);
            when(jwt.getJWTClaimsSet()).thenReturn(jwtClaimsSet);
            when(jwtClaimsSet.getExpirationTime()).thenReturn(Date.from(Instant.now().minus(1, ChronoUnit.MINUTES)));
            when(jwt.verify(any(JWSVerifier.class))).thenReturn(true);
            when(tokenRepository.existsById(any())).thenReturn(true);

            var exception = assertThrows(AppException.class,
                    ()->authService.instropect(request));

            assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.AUTHENTICATION);
        }
    }

    @Test
    void instropect_fail_isTime() throws ParseException, JOSEException {
        try (MockedStatic<SignedJWT> mocked = mockStatic(SignedJWT.class)) {
            TokenRequest request = mock(TokenRequest.class);
            SignedJWT jwt = mock(SignedJWT.class);
            JWTClaimsSet jwtClaimsSet = mock(JWTClaimsSet.class);

            mocked.when(() -> SignedJWT.parse(request.getToken())).thenReturn(jwt);
            when(jwt.getJWTClaimsSet()).thenReturn(jwtClaimsSet);
            when(jwtClaimsSet.getExpirationTime()).thenReturn(Date.from(Instant.now().plus(1, ChronoUnit.MINUTES)));
            when(jwt.verify(any(JWSVerifier.class))).thenReturn(false);
            when(tokenRepository.existsById(any())).thenReturn(true);

            var exception = assertThrows(AppException.class,
                    ()->authService.instropect(request));

            assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.AUTHENTICATION);
        }
    }

    @Test
    void instropect_fail_isExists() throws ParseException, JOSEException {
        try (MockedStatic<SignedJWT> mocked = mockStatic(SignedJWT.class)) {
            TokenRequest request = mock(TokenRequest.class);
            SignedJWT jwt = mock(SignedJWT.class);
            JWTClaimsSet jwtClaimsSet = mock(JWTClaimsSet.class);

            mocked.when(() -> SignedJWT.parse(request.getToken())).thenReturn(jwt);
            when(jwt.getJWTClaimsSet()).thenReturn(jwtClaimsSet);
            when(jwtClaimsSet.getExpirationTime()).thenReturn(Date.from(Instant.now().plus(1, ChronoUnit.MINUTES)));
            when(jwt.verify(any(JWSVerifier.class))).thenReturn(true);
            when(tokenRepository.existsById(any())).thenReturn(false);

            var exception = assertThrows(AppException.class,
                    ()->authService.instropect(request));

            assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.AUTHENTICATION);
        }
    }

}
