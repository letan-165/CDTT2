package com.quizz.AccountService.Controller;


import com.quizz.AccountService.DTO.Request.EmailRequest;
import com.quizz.AccountService.Service.OtpService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import quizz.library.common.DTO.ApiResponse;

@RestController
@RequestMapping("/otp")
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class OtpController {
    OtpService otpService;

    @PostMapping("/public")
    ApiResponse<?> create(@RequestBody EmailRequest request){
        otpService.create(request,true);
        return ApiResponse.builder()
                .message("Vui lòng kiểm tra Email")
                .build();
    }

    @PostMapping
    ApiResponse<?> createTest(@RequestBody EmailRequest request){
        otpService.create(request,false);
        return ApiResponse.builder()
                .message("Xem log")
                .build();
    }
}
