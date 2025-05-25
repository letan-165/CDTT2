package com.app.QuizService.Repository.HttpClient;

import com.app.QuizService.DTO.ApiResponse;
import com.app.QuizService.DTO.Request.Client.SendNotificationRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "notification",url = "${app.service.notification}")
public interface NotificationClient {

    @PostMapping(value = "/personal/send",produces = MediaType.APPLICATION_JSON_VALUE)
    ApiResponse<?> sendNotification(@RequestBody SendNotificationRequest request);

}
