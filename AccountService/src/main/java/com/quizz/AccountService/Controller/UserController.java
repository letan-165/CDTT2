package com.quizz.AccountService.Controller;

import com.quizz.AccountService.DTO.Request.ForgotPassRequest;
import com.quizz.AccountService.DTO.Request.SaveLockUserRequest;
import com.quizz.AccountService.DTO.Request.UserSignUpRequest;
import com.quizz.AccountService.DTO.Response.UserResponse;
import com.quizz.AccountService.Entity.Redis.LockUser;
import com.quizz.AccountService.Service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import quizz.library.common.DTO.ApiResponse;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class UserController {
    UserService userService;

    @GetMapping("/public")
    ApiResponse<List<UserResponse>> findAll(){
        return ApiResponse.<List<UserResponse>>builder()
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

    @GetMapping("/public/id/{userID}")
    ApiResponse<UserResponse> findByID(@PathVariable String userID){
        return ApiResponse.<UserResponse>builder()
                .message("Tìm người dùng dựa vào ID:  "+userID)
                .result(userService.findById(userID))
                .build();
    }

    @GetMapping("/public/name/{name}")
    ApiResponse<UserResponse> findByName(@PathVariable String name){
        return ApiResponse.<UserResponse>builder()
                .message("Tìm người dùng dựa vào tên: "+name)
                .result(userService.findByName(name))
                .build();
    }

    @PutMapping("/public/forgotPassword")
    ApiResponse<UserResponse> forgotPassword(@RequestBody ForgotPassRequest request){
        return ApiResponse.<UserResponse>builder()
                .message("Người dùng "+request.getUsername() +"quên mật khẩu")
                .result(userService.forgotPassword(request))
                .build();
    }

    //USerLock
    @GetMapping("/public/lock")
    ApiResponse<List<LockUser>> findAllLock(){
        return ApiResponse.<List<LockUser>>builder()
                .message("Danh sách ngươi dùng bị khóa tam thời")
                .result(userService.findAllLock())
                .build();
    }

    @PostMapping("/public/lock")
    ApiResponse<LockUser> saveLockUser(@RequestBody SaveLockUserRequest request){
        return ApiResponse.<LockUser>builder()
                .message("Khởi tạo người dùng bị khóa: "+request.getUserID())
                .result(userService.saveLockUser(request))
                .build();
    }

    @DeleteMapping("/public/lock/{userID}")
    ApiResponse<Boolean> deleteLockUser(@PathVariable String userID){
        return ApiResponse.<Boolean>builder()
                .message("Mở khóa người dùng: "+userID)
                .result(userService.deleteLockUser(userID))
                .build();
    }
}
