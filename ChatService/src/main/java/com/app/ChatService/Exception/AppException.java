package com.app.ChatService.Exception;

import lombok.Data;

@Data
public class AppException extends RuntimeException{
    private ErrorCode errorCode;
    public AppException(ErrorCode e){
        super(e.getMessage());
        this.errorCode = e;
    }
}
