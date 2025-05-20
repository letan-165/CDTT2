package com.app.QuizService.Exception;

import com.app.QuizService.DTO.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
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
    ResponseEntity<ApiResponse> handlingException(Exception e){
        return toResponseEntity(ErrorCode.OTHER_ERROL);
    }

    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ApiResponse> HandlingAppException(AppException e){
        return toResponseEntity(e.errorCode);


    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<ApiResponse> handlingDefaultMessageSourceResolvable(MethodArgumentNotValidException e){
        String error = e.getBindingResult()
                .getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .findFirst()
                .orElse("OTHER_ERROL");

        return toResponseEntity(ErrorCode.valueOf(error));
    }


}
