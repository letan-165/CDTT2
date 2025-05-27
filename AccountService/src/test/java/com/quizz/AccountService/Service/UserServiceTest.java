package com.quizz.AccountService.Service;

import com.quizz.AccountService.DTO.Request.ForgotPassRequest;
import com.quizz.AccountService.DTO.Request.SaveLockUserRequest;
import com.quizz.AccountService.DTO.Request.UserSignUpRequest;
import com.quizz.AccountService.DTO.Response.UserResponse;
import com.quizz.AccountService.Entity.MySql.Role;
import com.quizz.AccountService.Entity.MySql.User;
import com.quizz.AccountService.Exception.AppException;
import com.quizz.AccountService.Exception.ErrorCode;
import com.quizz.AccountService.Mapper.UserMapper;
import com.quizz.AccountService.Repository.MySql.RoleRepository;
import com.quizz.AccountService.Repository.MySql.UserRepository;
import com.quizz.AccountService.Repository.Redis.LockUserRepository;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserServiceTest {
    @InjectMocks
    UserService userService;

    @Mock UserRepository userRepository;
    @Mock RoleRepository roleRepository;
    @Mock OtpService otpService;
    @Mock PasswordEncoder passwordEncoder;
    @Mock UserMapper userMapper;
    @Mock LockUserRepository lockUserRepository;

    @Mock Role role;
    @Mock User user;
    @Mock UserResponse userResponse;
    @Mock UserSignUpRequest userSignUpRequest;
    @Mock ForgotPassRequest forgotPassRequest;

    @Test
    void signUpTest_success(){
        String encodePassword = "123";
        when(userRepository.existsByName(eq(userSignUpRequest.getName()))).thenReturn(false);
        when(roleRepository.findById(eq(userSignUpRequest.getRole()))).thenReturn(Optional.of(role));
        when(userMapper.toUser(eq(userSignUpRequest))).thenReturn(user);
        when(passwordEncoder.encode(eq(userSignUpRequest.getPassword()))).thenReturn(encodePassword);
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toUserResponse(user)).thenReturn(userResponse);

        UserResponse response = userService.signUp(userSignUpRequest);

        verify(user).setRole(role);
        verify(user).setPassword(encodePassword);

        assertThat(response).isEqualTo(userResponse);
    }

    @Test
    void signUpTest_fail_USER_EXIST(){
        when(userRepository.existsByName(eq(userSignUpRequest.getName()))).thenReturn(true);

        var exception = assertThrows(AppException.class,
                ()->userService.signUp(userSignUpRequest));

        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.USER_EXIST);
    }

    @Test
    void signUpTest_fail_ROLE_INVALID(){
        when(userRepository.existsByName(eq(userSignUpRequest.getName()))).thenReturn(false);
        when(roleRepository.findById(eq(userSignUpRequest.getRole()))).thenReturn(Optional.empty());
        var exception = assertThrows(AppException.class,
                ()->userService.signUp(userSignUpRequest));

        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.ROLE_INVALID);
    }

    @Test
    void forgotPassword_success(){
        String encodePassword = "123";
        String emailUser = "a@b";
        int otp = 100000;
        when(userRepository.findByName(eq(forgotPassRequest.getUsername()))).thenReturn(Optional.of(user));
        when(user.getEmail()).thenReturn(emailUser);
        when(forgotPassRequest.getEmail()).thenReturn("a@b");
        when(forgotPassRequest.getOtp()).thenReturn(otp);
        when(passwordEncoder.encode(eq(forgotPassRequest.getPassword()))).thenReturn(encodePassword);
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toUserResponse(user)).thenReturn(userResponse);

        UserResponse response = userService.forgotPassword(forgotPassRequest);

        verify(otpService).verify(eq(emailUser), eq(otp));
        verify(user).setPassword(eq(encodePassword));

        assertThat(response).isEqualTo(userResponse);

    }

    @Test
    void forgotPassword_fail_USER_NO_EXIST(){
        when(userRepository.findByName(eq(forgotPassRequest.getUsername()))).thenReturn(Optional.empty());

        var exception = assertThrows(AppException.class,
                ()->userService.forgotPassword(forgotPassRequest));

        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.USER_NO_EXIST);

    }

    @Test
    void forgotPassword_fail_EMAIL_INVALID(){
        String emailUser = "a@b";
        when(userRepository.findByName(eq(forgotPassRequest.getUsername()))).thenReturn(Optional.of(user));
        when(user.getEmail()).thenReturn(emailUser);
        when(forgotPassRequest.getEmail()).thenReturn("a@bc");
        var exception = assertThrows(AppException.class,
                ()->userService.forgotPassword(forgotPassRequest));

        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.EMAIL_INVALID);
    }

}
