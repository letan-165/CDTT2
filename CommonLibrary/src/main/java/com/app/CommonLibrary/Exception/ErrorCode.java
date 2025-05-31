package com.app.CommonLibrary.Exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    QUESTION_NO_EXISTS(1024,"Lỗi không có câu hỏi trong bài quiz", HttpStatus.BAD_REQUEST),
    TIME_TODO_INVALID(1023,"Lỗi chưa đến thời gian làm bài", HttpStatus.BAD_REQUEST),
    NOTIFICATION_NO_EXISTS(1022,"Lỗi thông báo chưa đươợc khởi tạo", HttpStatus.BAD_REQUEST),
    USER_LOCK(1021,"Tài khoản hiện đang bị khóa tạm thời", HttpStatus.BAD_REQUEST),
    USER_NO_LOCK(1020,"Tài khoản không nằm trong danh sách bị khóa", HttpStatus.BAD_REQUEST),
    EMAIL_INVALID(1019,"Xác thực emai không hợp lệ", HttpStatus.BAD_REQUEST),
    OTP_INVALID(1018,"Xác thực otp không hợp lệ", HttpStatus.BAD_REQUEST),
    DURATION_FIELD_INVALID(1017,"Khoảng cách giữa 2 thời gian nhỏ hơn tổng thời gian làm bài", HttpStatus.BAD_REQUEST),
    BETWEEN_TIME_INVALID(1016,"Thời gian bắt đầu nằm sau thời gian kết thúc", HttpStatus.BAD_REQUEST),
    FIELD_TIME_NOTNULL(1015,"Các giá trị thời gian chưa nhập đầy đủ", HttpStatus.BAD_REQUEST),
    RESULT_NO_EXISTS(1014,"Lỗi kết quả chưa được khởi tạo trong hệ thống", HttpStatus.BAD_REQUEST),
    QUIZ_NO_EXISTS(1013,"Lỗi bài quiz không tồn tại trong hệ thống", HttpStatus.BAD_REQUEST),
    CORRECT_INVALID(1012,"Lỗi câu đúng không hợp lệ", HttpStatus.BAD_REQUEST),
    USER_IMPOSSIBLE_RECALL_CHAT(1011,"Bạn không có quyền thu hồi tin nhắn", HttpStatus.BAD_REQUEST),
    USER_NO_EXISTS_CHAT(1010,"Người dùng không tồn tại trong đoạn chat ", HttpStatus.BAD_REQUEST),
    CHAT_NO_EXISTS(1009,"Chat giữa 2 người dùng chưa được khởi tạo", HttpStatus.BAD_REQUEST),
    USERID_DUPLICATE(1008,"Trùng lặp id người dùng khi gửi tin nhắn", HttpStatus.BAD_REQUEST),
    CHATBOT_NO_EXISTS(1007,"ChatBot chưa được khởi tạo", HttpStatus.BAD_REQUEST),
    ROLE_INVALID(1006,"Lỗi vai trò người dùng (role) không hợp lệ", HttpStatus.BAD_REQUEST),
    PASSWORD_LOGIN_FAIL(1005,"Lỗi người dùng (user) đăng nhập sai mật khẩu", HttpStatus.BAD_REQUEST),
    USER_EXIST(1004,"Lỗi người dùng (user) đã tồn tại trong hệ thống", HttpStatus.BAD_REQUEST),
    USER_NO_EXIST(1003,"Lỗi không tìm thấy người dùng (user)", HttpStatus.BAD_REQUEST),
    AUTHENTICATION(1002,"Lỗi xác thực không đúng", HttpStatus.UNAUTHORIZED),
    AUTHORIZED(1001,"Lỗi không có quyền", HttpStatus.FORBIDDEN),
    OTHER_ERROL(9999,"Lỗi mới chưa định nghĩa", HttpStatus.INTERNAL_SERVER_ERROR);

    int code;
    String message;
    HttpStatusCode httpStatusCode;

}
