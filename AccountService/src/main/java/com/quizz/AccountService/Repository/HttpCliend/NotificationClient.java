package com.quizz.AccountService.Repository.HttpCliend;

import com.app.CommonLibrary.DTO.ApiResponse;
import com.quizz.AccountService.DTO.Request.Client.SendEmailRequest;
import com.quizz.AccountService.DTO.Response.Client.EmailResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "notification",url = "${app.services.notification}")
public interface NotificationClient {

    @PostMapping(value = "/email/send",produces = MediaType.APPLICATION_JSON_VALUE)
    ApiResponse<EmailResponse> sendEmail(@RequestBody SendEmailRequest request);

}
