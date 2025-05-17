package com.quizz.AccountService.Controller;

import com.quizz.AccountService.DTO.ApiResponse;
import com.quizz.AccountService.DTO.Request.UserSignUpRequest;
import com.quizz.AccountService.DTO.Response.UserResponse;
import com.quizz.AccountService.Entity.User;
import com.quizz.AccountService.Service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class UserController {
    UserService userService;

    @GetMapping()
    ApiResponse<List<User>> findAll(){
        return ApiResponse.<List<User>>builder()
                .result(userService.findAll())
                .build();
    }

    @PostMapping("/public")
    ApiResponse<UserResponse> signUp(@RequestBody UserSignUpRequest request){
        return ApiResponse.<UserResponse>builder()
                .message("Đăng kí người dùng: "+request.getName())
                .result(userService.signUp(request))
                .build();
    }

    @GetMapping("/public/id={userID}")
    ApiResponse<UserResponse> findByID(@PathVariable String userID){
        return ApiResponse.<UserResponse>builder()
                .message("Tìm người dùng dựa vào ID:  "+userID)
                .result(userService.findById(userID))
                .build();
    }

    @GetMapping("/public/name={name}")
    ApiResponse<UserResponse> findByName(@PathVariable String name){
        return ApiResponse.<UserResponse>builder()
                .message("Tìm người dùng dựa vào tên: "+name)
                .result(userService.findByName(name))
                .build();
    }
}
