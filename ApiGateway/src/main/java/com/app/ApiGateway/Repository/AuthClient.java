package com.app.ApiGateway.Repository;

import com.app.ApiGateway.DTO.ApiResponse;
import com.app.ApiGateway.DTO.Request.TokenRequest;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.PostExchange;
import reactor.core.publisher.Mono;

public interface AuthClient {
    @PostExchange(url = "/auth/instropect",contentType = MediaType.APPLICATION_JSON_VALUE)
    Mono<ApiResponse<Boolean>> instropect(@RequestBody TokenRequest request);
}
