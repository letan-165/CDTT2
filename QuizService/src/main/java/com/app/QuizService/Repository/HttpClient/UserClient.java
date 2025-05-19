package com.app.QuizService.Repository.HttpClient;

import com.app.QuizService.DTO.ApiResponse;
import com.app.QuizService.DTO.Response.Client.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user", url = "${app.service.account}")
public interface UserClient {
    @GetMapping(value = "/user/public/id={userID}", produces = MediaType.APPLICATION_JSON_VALUE)
    ApiResponse<UserResponse> findById (@PathVariable String userID);

    @GetMapping(value = "/user/public/name={name}", produces = MediaType.APPLICATION_JSON_VALUE)
    ApiResponse<UserResponse> findByName (@PathVariable String name);

}
