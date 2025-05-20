package com.app.QuizService.Exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    RESULT_NO_EXISTS(1014,"Lỗi kết quả chưa được khởi tạo trong hệ thống", HttpStatus.BAD_REQUEST),
    QUIZ_NO_EXISTS(1013,"Lỗi bài quiz không tồn tại trong hệ thống", HttpStatus.BAD_REQUEST),
    CORRECT_INVALID(1012,"Thứ tự câu đúng không hợp lệ", HttpStatus.BAD_REQUEST),
    AUTHENTICATION(1002,"Lỗi xác thực không đúng", HttpStatus.UNAUTHORIZED),
    AUTHORIZED(1001,"Lỗi không có quyền", HttpStatus.FORBIDDEN),
    OTHER_ERROL(9999,"Lỗi mới chưa định nghĩa", HttpStatus.INTERNAL_SERVER_ERROR);

    int code;
    String message;
    HttpStatusCode httpStatusCode;

}
