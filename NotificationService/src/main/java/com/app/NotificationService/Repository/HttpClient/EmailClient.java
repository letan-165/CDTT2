package com.app.NotificationService.Repository.HttpClient;

import com.app.NotificationService.DTO.Request.Email.EmailFullRequest;
import com.app.NotificationService.DTO.Response.Email.EmailResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "email", url = "${app.client.smtp}")
public interface EmailClient {

    @PostMapping(value = "/email", produces = MediaType.APPLICATION_JSON_VALUE)
    EmailResponse sendEmail(@RequestHeader("api-key") String key, @RequestBody EmailFullRequest request);

}
