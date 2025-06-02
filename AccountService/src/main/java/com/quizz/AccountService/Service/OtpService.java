package com.quizz.AccountService.Service;


import com.quizz.AccountService.DTO.Request.Client.SendEmailRequest;
import com.quizz.AccountService.DTO.Request.Client.Sender;
import com.quizz.AccountService.DTO.Request.EmailRequest;
import com.quizz.AccountService.Entity.Redis.Otp;
import com.quizz.AccountService.Repository.HttpCliend.NotificationClient;
import com.quizz.AccountService.Repository.Redis.OtpRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import quizz.library.common.Exception.AppException;
import quizz.library.common.Exception.ErrorCode;

import java.security.SecureRandom;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class OtpService {
    OtpRepository otpRepository;
    NotificationClient  notificationClient;

    public void create(EmailRequest request, boolean isEmail){
        //Delete old before create new otp
        otpRepository.deleteById(request.getEmail());
        SecureRandom secureRandom = new SecureRandom();
        int random = 100000 + secureRandom.nextInt(900000);
        Otp otp = otpRepository.save(Otp.builder()
                        .email(request.getEmail())
                        .value(random)
                        .expiryTime(300)
                .build());

        if(isEmail){
            notificationClient.sendEmail(SendEmailRequest.builder()
                            .to(Sender.builder()
                                    .email(request.getEmail())
                                    .build())
                        .subject("Xác thực OTP")
                        .content("Mã xác thực (OTP) của bạn là: " + random +
                                "<br/>Mã này có hiệu lực trong 5 phút.   " +
                                "<br/><br/>Vui lòng không chia sẻ mã này với bất kỳ ai.")
                    .build());
        }else {
            log.info("OTP: {}",otp.getValue());
        }
    }

    public void verify(String email, int otp) {
        Otp otpE = otpRepository.findById(email)
                .orElseThrow(()->new AppException(ErrorCode.OTP_INVALID));
        if(otpE.getValue() != otp)
            throw new AppException(ErrorCode.OTP_INVALID);

        otpRepository.deleteById(email);
    }
}
