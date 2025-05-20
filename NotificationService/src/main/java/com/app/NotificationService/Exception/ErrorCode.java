package com.app.NotificationService.Exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    AUTHENTICATION(1010,"Token not authentication ", HttpStatus.UNAUTHORIZED),
    AUTHORIZED(1009,"You don't have permission", HttpStatus.FORBIDDEN),
    OTHER_ERROL(9999,"Other errol", HttpStatus.INTERNAL_SERVER_ERROR),;

    int code;
    String message;
    HttpStatus  httpStatus;
}
