package com.app.ChatService.Exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    USER_IMPOSSIBLE_RECALL_CHAT(1011,"Bạn không có quyền thu hồi tin nhắn", HttpStatus.BAD_REQUEST),
    USER_NO_EXISTS_CHAT(1010,"Người dùng không tồn tại trong đoạn chat ", HttpStatus.BAD_REQUEST),
    CHAT_NO_EXISTS(1009,"Chat giữa 2 người dùng chưa được khởi tạo", HttpStatus.BAD_REQUEST),
    USERID_DUPLICATE(1008,"Trùng lặp id người dùng khi gửi tin nhắn", HttpStatus.BAD_REQUEST),
    CHATBOT_NO_EXISTS(1007,"ChatBot chưa được khởi tạo", HttpStatus.BAD_REQUEST),
    USER_NO_EXIST(1003,"Lỗi không tìm thấy người dùng (user)", HttpStatus.BAD_REQUEST),
    AUTHENTICATION(1002,"Lỗi xác thực không đúng", HttpStatus.UNAUTHORIZED),
    AUTHORIZED(1001,"Lỗi không có quyền", HttpStatus.FORBIDDEN),
    OTHER_ERROL(9999,"Lỗi mới chưa định nghĩa", HttpStatus.INTERNAL_SERVER_ERROR),;

    int code;
    String message;
    HttpStatus  httpStatus;
}
