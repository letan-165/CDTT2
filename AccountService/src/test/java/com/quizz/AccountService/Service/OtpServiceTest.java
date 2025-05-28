package com.quizz.AccountService.Service;

import com.quizz.AccountService.DTO.Request.Client.SendEmailRequest;
import com.quizz.AccountService.DTO.Request.EmailRequest;
import com.quizz.AccountService.Entity.Redis.Otp;
import com.quizz.AccountService.Exception.AppException;
import com.quizz.AccountService.Exception.ErrorCode;
import com.quizz.AccountService.Repository.HttpCliend.NotificationClient;
import com.quizz.AccountService.Repository.Redis.OtpRepository;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OtpServiceTest {
    @InjectMocks
    OtpService otpService;

    @Mock OtpRepository otpRepository;
    @Mock NotificationClient notificationClient;

    @Mock EmailRequest emailRequest;
    @Mock Otp otp;

    @Test
    void create_success_isEmail_true(){
        String email = "a@bc";
        when(emailRequest.getEmail()).thenReturn(email);

        otpService.create(emailRequest,true);

        verify(otpRepository).deleteById(eq(email));
        verify(otpRepository).save(any(Otp.class));
        verify(notificationClient).sendEmail(any(SendEmailRequest.class));
    }

    @Test
    void create_success_isEmail_false(){
        String email = "a@bc";
        when(emailRequest.getEmail()).thenReturn(email);
        when(otpRepository.save(any(Otp.class))).thenReturn(otp);

        otpService.create(emailRequest,false);

        verify(otpRepository).deleteById(eq(email));
        verify(otpRepository).save(any(Otp.class));
        verify(notificationClient,never()).sendEmail(any(SendEmailRequest.class));
    }

    @Test
    void verify_success(){
        String email = "a@b";
        int value = otp.getValue();
        when(otpRepository.findById(eq(email))).thenReturn(Optional.of(otp));

        otpService.verify(email,value);

        verify(otpRepository).deleteById(eq(email));
    }

    @Test
    void verify_fail_OTP_INVALID_NO_EXISTS(){
        String email = "a@b";
        int value = otp.getValue();
        when(otpRepository.findById(eq(email))).thenReturn(Optional.empty());

        var exception = assertThrows(AppException.class,()->otpService.verify(email,value));
        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.OTP_INVALID);
    }

    @Test
    void verify_fail_OTP_INVALID_NO_EQUAL(){
        String email = "a@b";
        int value = 123456;
        when(otpRepository.findById(eq(email))).thenReturn(Optional.of(otp));

        var exception = assertThrows(AppException.class,()->otpService.verify(email,value));
        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.OTP_INVALID);
    }

}
