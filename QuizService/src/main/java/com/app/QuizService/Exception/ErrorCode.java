package com.app.QuizService.Exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    QUESTION_NO_EXISTS(1023,"Lỗi không có câu hỏi trong bài quiz", HttpStatus.BAD_REQUEST),
    TIME_TODO_INVALID(1021,"Lỗi chưa đến thời gian làm bài", HttpStatus.BAD_REQUEST),
    DURATION_FIELD_INVALID(1017,"Khoảng cách giữa 2 thời gian nhỏ hơn tổng thời gian làm bài", HttpStatus.BAD_REQUEST),
    BETWEEN_TIME_INVALID(1016,"Thời gian bắt đầu nằm sau thời gian kết thúc", HttpStatus.BAD_REQUEST),
    FIELD_TIME_NOTNULL(1015,"Các giá trị thời gian chưa nhập đầy đủ", HttpStatus.BAD_REQUEST),
    RESULT_NO_EXISTS(1014,"Lỗi kết quả chưa được khởi tạo trong hệ thống", HttpStatus.BAD_REQUEST),
    QUIZ_NO_EXISTS(1013,"Lỗi bài quiz không tồn tại trong hệ thống", HttpStatus.BAD_REQUEST),
    CORRECT_INVALID(1012,"Lỗi câu đúng không hợp lệ", HttpStatus.BAD_REQUEST),
    USER_NO_EXIST(1003,"Lỗi không tìm thấy người dùng (user)", HttpStatus.BAD_REQUEST),
    AUTHENTICATION(1002,"Lỗi xác thực không đúng", HttpStatus.UNAUTHORIZED),
    AUTHORIZED(1001,"Lỗi không có quyền", HttpStatus.FORBIDDEN),
    OTHER_ERROL(9999,"Lỗi mới chưa định nghĩa", HttpStatus.INTERNAL_SERVER_ERROR);

    int code;
    String message;
    HttpStatusCode httpStatusCode;

}
