package com.app.NotificationService.Controller;

import com.app.NotificationService.DTO.ApiResponse;
import com.app.NotificationService.DTO.Request.Email.SendEmailRequest;
import com.app.NotificationService.DTO.Response.Email.EmailResponse;
import com.app.NotificationService.Service.EmailService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/email")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@Slf4j
public class EmailController {
    EmailService emailService;

    @PostMapping("/send")
    ApiResponse<EmailResponse> sendEmail(@RequestBody SendEmailRequest request){
        return ApiResponse.<EmailResponse>builder()
                .message("Gửi gmail đến "+request.getTo().getEmail())
                .result(emailService.sendEmail(request))
                .build();
    }

}
