package com.app.ApiGateway.Service;

import com.app.ApiGateway.DTO.ApiResponse;
import com.app.ApiGateway.DTO.Request.TokenRequest;
import com.app.ApiGateway.Repository.AuthClient;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@Slf4j
public class AuthService {
    AuthClient authClient;

    public Mono<ApiResponse<Boolean>> instropect(String token){
        return authClient.instropect(TokenRequest.builder()
                        .token(token)
                .build());
    }
}
