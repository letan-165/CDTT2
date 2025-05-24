package com.quizz.AccountService.Exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    USER_LOCK(1021,"Tài khoản hiện đang bị khóa tạm thời", HttpStatus.BAD_REQUEST),
    USER_NO_LOCK(1020,"Tài khoản không nằm trong danh sách bị khóa", HttpStatus.BAD_REQUEST),
    EMAIL_INVALID(1019,"Xác thực emai không hợp lệ", HttpStatus.BAD_REQUEST),
    OTP_INVALID(1018,"Xác thực otp không hợp lệ", HttpStatus.BAD_REQUEST),
    PARSE_TOKEN_FAIL(1007,"Lỗi định dạng token không hợp lệ", HttpStatus.BAD_REQUEST),
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
