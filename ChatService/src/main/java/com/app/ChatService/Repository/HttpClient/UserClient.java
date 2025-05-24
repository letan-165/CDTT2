package com.app.ChatService.Repository.HttpClient;

import com.app.ChatService.DTO.ApiResponse;
import com.app.ChatService.DTO.Request.Client.TokenRequest;
import com.app.ChatService.DTO.Response.Client.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "user",url = "${app.service.account}")
public interface UserClient {
    @GetMapping(value = "/user/public/id/{userID}", produces = MediaType.APPLICATION_JSON_VALUE)
    ApiResponse<UserResponse> findById (@PathVariable String userID);

    @GetMapping(value = "/user/public/name/{name}", produces = MediaType.APPLICATION_JSON_VALUE)
    ApiResponse<UserResponse> findByName (@PathVariable String name);

    @PostMapping(value = "/auth/public/findName", produces = MediaType.APPLICATION_JSON_VALUE)
    ApiResponse<String> findNameFromToken (@RequestBody TokenRequest request);
}
