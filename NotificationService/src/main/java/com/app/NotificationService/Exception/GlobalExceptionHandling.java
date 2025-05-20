package com.app.NotificationService.Exception;

import com.app.NotificationService.DTO.ApiResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandling {

    ResponseEntity<ApiResponse> toResponseEntity(ErrorCode errorCode){
        return ResponseEntity.status(errorCode.httpStatus)
                .body(ApiResponse.builder()
                        .code(errorCode.code)
                        .message(errorCode.message)
                        .build());
    }

    @ExceptionHandler(value = Exception.class)
    ResponseEntity<ApiResponse> handlingException(){
        return toResponseEntity(ErrorCode.OTHER_ERROL);
    }

    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ApiResponse> handlingAppException(AppException exception){
        ErrorCode errorCode = exception.getErrorCode();
        return toResponseEntity(errorCode);
    }

    @ExceptionHandler(value = AuthorizationDeniedException.class)
    ResponseEntity<ApiResponse> handlingDeniedException(){
        return toResponseEntity(ErrorCode.AUTHORIZED);
    }

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<ApiResponse> handleFeignException(FeignException e) {
        String responseEx = e.contentUTF8();
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> errol = null;
        try {
            errol = objectMapper.readValue(responseEx, new TypeReference<Map<String, Object>>() {});
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.builder()
                            .code(90000 + (Integer) errol.get("code"))
                            .message((String)errol.get("message"))
                            .build());
        } catch (JsonProcessingException ex) {
            return toResponseEntity(ErrorCode.OTHER_ERROL);
        }


    }





}
