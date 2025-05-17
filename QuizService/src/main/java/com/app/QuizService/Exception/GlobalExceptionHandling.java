package com.app.QuizService.Exception;

import com.app.QuizService.DTO.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandling {

    ResponseEntity<ApiResponse> toResponseEntity(ErrorCode errorCode){
        return ResponseEntity.status(errorCode.httpStatusCode)
                .body(ApiResponse.builder()
                        .code(errorCode.code)
                        .message(errorCode.message)
                        .build());
    }

    @ExceptionHandler(value = Exception.class)
    ResponseEntity<ApiResponse> HandlingException(Exception e){
        return toResponseEntity(ErrorCode.OTHER_ERROL);
    }

    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ApiResponse> HandlingAppException(AppException e){
        return toResponseEntity(e.errorCode);
    }


}
