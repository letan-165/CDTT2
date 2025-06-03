package com.quizz.AccountService.Controller;

import quizz.library.common.Exception.AppException;
import quizz.library.common.Exception.ErrorCode;
import com.quizz.AccountService.DTO.Request.ForgotPassRequest;
import com.quizz.AccountService.DTO.Request.SaveLockUserRequest;
import com.quizz.AccountService.DTO.Request.UserSignUpRequest;
import com.quizz.AccountService.DTO.Response.UserResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.quizz.AccountService.Entity.Redis.LockUser;
import com.quizz.AccountService.Service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    UserService userService;

    UserResponse userResponse;
    LockUser lockUser;

    UserSignUpRequest userSignUpRequest;
    ForgotPassRequest forgotPassRequest;

    @BeforeEach
    void initData(){
        userResponse = UserResponse.builder()
                .userID("tanID")
                .name("tan")
                .phone("0909")
                .email("a@b")
                .role("ADMIN")
                .build();
        userSignUpRequest = UserSignUpRequest.builder()
                .name("tan")
                .phone("0909")
                .email("a@b")
                .role("ADMIN")
                .build();
        forgotPassRequest = ForgotPassRequest.builder()
                .username("tan")
                .email("a@b")
                .password("123")
                .otp(123456)
                .build();
        lockUser = LockUser.builder()
                .userID("tanID")
                .name("tan")
                .expiryTime(20)
                .build();
    }

    @Test
    void findAll_success() throws Exception {
        List<UserResponse> userResponseList = new ArrayList<>(List.of(userResponse,userResponse));

        when(userService.findAll()).thenReturn(userResponseList);

        mockMvc.perform(get("/user/public"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("code").value(1000))
                .andExpect(jsonPath("result.length()").value(2));
    }

    @Test
    void signUp_success() throws Exception {
        var content = objectMapper.writeValueAsString(userSignUpRequest);

        when(userService.signUp(userSignUpRequest)).thenReturn(userResponse);

        mockMvc.perform(post("/user/public")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content))
                .andExpect(status().isOk())
                .andExpect(jsonPath("code").value(1000))
                .andExpect(jsonPath("result.userID").value(userResponse.getUserID()))
                .andExpect(jsonPath("result.name").value(userResponse.getName()))
                .andExpect(jsonPath("result.phone").value(userResponse.getPhone()))
                .andExpect(jsonPath("result.email").value(userResponse.getEmail()))
                .andExpect(jsonPath("result.role").value(userResponse.getRole()));

    }

    @Test
    void signUp_fail_USER_EXIST() throws Exception {
        ErrorCode errorCode = ErrorCode.USER_EXIST;
        var content = objectMapper.writeValueAsString(userSignUpRequest);

        when(userService.signUp(userSignUpRequest))
                .thenThrow(new AppException(errorCode));

        mockMvc.perform(post("/user/public")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("code").value(errorCode.getCode()))
                .andExpect(jsonPath("message").value(errorCode.getMessage()));
    }

    @Test
    void signUp_fail_ROLE_INVALID() throws Exception {
        ErrorCode errorCode = ErrorCode.ROLE_INVALID;
        var content = objectMapper.writeValueAsString(userSignUpRequest);

        when(userService.signUp(userSignUpRequest))
                .thenThrow(new AppException(errorCode));

        mockMvc.perform(post("/user/public")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("code").value(errorCode.getCode()))
                .andExpect(jsonPath("message").value(errorCode.getMessage()));
    }

    @Test
    void findByID_success() throws Exception {
        String userID = userResponse.getUserID();

        when(userService.findById(userID)).thenReturn(userResponse);

        mockMvc.perform(get("/user/public/id/"+userID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("code").value(1000))
                .andExpect(jsonPath("result.userID").value(userResponse.getUserID()))
                .andExpect(jsonPath("result.name").value(userResponse.getName()))
                .andExpect(jsonPath("result.phone").value(userResponse.getPhone()))
                .andExpect(jsonPath("result.email").value(userResponse.getEmail()))
                .andExpect(jsonPath("result.role").value(userResponse.getRole()));

    }

    @Test
    void findByName_success() throws Exception {
        String name = userResponse.getName();

        when(userService.findByName(name)).thenReturn(userResponse);

        mockMvc.perform(get("/user/public/name/"+name))
                .andExpect(status().isOk())
                .andExpect(jsonPath("code").value(1000))
                .andExpect(jsonPath("result.userID").value(userResponse.getUserID()))
                .andExpect(jsonPath("result.name").value(userResponse.getName()))
                .andExpect(jsonPath("result.phone").value(userResponse.getPhone()))
                .andExpect(jsonPath("result.email").value(userResponse.getEmail()))
                .andExpect(jsonPath("result.role").value(userResponse.getRole()));

    }

    @Test
    void forgotPassword_success() throws Exception {
        String name = userResponse.getName();
        var content = objectMapper.writeValueAsString(forgotPassRequest);

        when(userService.forgotPassword(forgotPassRequest)).thenReturn(userResponse);

        mockMvc.perform(put("/user/public/forgotPassword")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content))
                .andExpect(status().isOk())
                .andExpect(jsonPath("code").value(1000))
                .andExpect(jsonPath("result.userID").value(userResponse.getUserID()))
                .andExpect(jsonPath("result.name").value(userResponse.getName()))
                .andExpect(jsonPath("result.phone").value(userResponse.getPhone()))
                .andExpect(jsonPath("result.email").value(userResponse.getEmail()))
                .andExpect(jsonPath("result.role").value(userResponse.getRole()));

    }

    @Test
    void findAllLock_success() throws Exception {
        List<LockUser> userResponseList = new ArrayList<>(List.of(lockUser,lockUser));

        when(userService.findAllLock()).thenReturn(userResponseList);

        mockMvc.perform(get("/user/public/lock"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("code").value(1000))
                .andExpect(jsonPath("result.length()").value(2));
    }

    @Test
    void saveLockUser_success() throws Exception {
        SaveLockUserRequest request = new SaveLockUserRequest();
        var content = objectMapper.writeValueAsString(request);

        when(userService.saveLockUser(request)).thenReturn(lockUser);

        mockMvc.perform(post("/user/public/lock")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content))
                .andExpect(status().isOk())
                .andExpect(jsonPath("code").value(1000))
                .andExpect(jsonPath("result.userID").value(lockUser.getUserID()))
                .andExpect(jsonPath("result.name").value(lockUser.getName()))
                .andExpect(jsonPath("result.expiryTime").value(lockUser.getExpiryTime()));

    }

    @Test
    void deleteLockUser_success() throws Exception {
        String userID = userResponse .getUserID();

        when(userService.deleteLockUser(userID)).thenReturn(true);

        mockMvc.perform(delete("/user/public/lock/"+userID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("code").value(1000))
                .andExpect(jsonPath("result").value(true));

    }
}
