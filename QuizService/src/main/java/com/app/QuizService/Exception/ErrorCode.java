package com.app.QuizService.Exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    AUTHENTICATION(1002,"Lỗi xác thực không đúng", HttpStatus.UNAUTHORIZED),
    AUTHORIZED(1001,"Lỗi không có quyền", HttpStatus.FORBIDDEN),
    OTHER_ERROL(9999,"Lỗi mới chưa định nghĩa", HttpStatus.INTERNAL_SERVER_ERROR);

    int code;
    String message;
    HttpStatusCode httpStatusCode;

}
