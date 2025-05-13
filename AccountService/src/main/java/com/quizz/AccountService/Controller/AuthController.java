package com.quizz.AccountService.Controller;

import com.nimbusds.jose.JOSEException;
import com.quizz.AccountService.DTO.ApiResponse;
import com.quizz.AccountService.DTO.Request.LoginRequest;
import com.quizz.AccountService.Service.AuthService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
