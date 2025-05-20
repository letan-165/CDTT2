package com.quizz.AccountService.Controller;

import com.nimbusds.jose.JOSEException;
import com.quizz.AccountService.DTO.ApiResponse;
import com.quizz.AccountService.DTO.Request.LoginRequest;
import com.quizz.AccountService.DTO.Request.TokenRequest;
import com.quizz.AccountService.Service.AuthService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class AuthController {
    AuthService authService;

    @PostMapping("/public/login")
    ApiResponse<String> login(@RequestBody LoginRequest request) throws JOSEException {
        return ApiResponse.<String>builder()
                .message("Xác nhận đăng nhập tài khoản: "+request.getUsername())
                .result(authService.login(request))
                .build();
    }

    @PostMapping("/public/findName")
    ApiResponse<String> findName(@RequestBody TokenRequest request) {
        return ApiResponse.<String>builder()
                .message("Tìm tên người dùng từ token: "+request.getToken())
                .result(authService.findName(request.getToken()))
                .build();
    }

    @PostMapping("/instropect")
    public ApiResponse<Boolean> instropect(@RequestBody TokenRequest request) throws ParseException, JOSEException {
        return ApiResponse.<Boolean>builder()
                .result(authService.instropect(request))
                .build();
    }

}
