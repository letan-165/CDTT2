package com.quizz.AccountService.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.quizz.AccountService.DTO.Request.LoginRequest;
import com.quizz.AccountService.DTO.Request.TokenRequest;
import com.quizz.AccountService.Service.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    AuthService authService;

    @Test
    void login_success() throws Exception {
        String token = "token123";
        LoginRequest request = LoginRequest.builder()
                .username("tan")
                .password("123")
                .build();
        String content = objectMapper.writeValueAsString(request);

        when(authService.login(request)).thenReturn(token);

        mockMvc.perform(post("/auth/public/login")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(content))
                .andExpect(status().isOk())
                .andExpect(jsonPath("code").value(1000))
                .andExpect(jsonPath("result").value(token));
    }

    @Test
    void findName_success() throws Exception {
        String token = "token123";
        String name = "tan";
        TokenRequest request = TokenRequest.builder()
                .token(token)
                .build();
        String content = objectMapper.writeValueAsString(request);

        when(authService.findName(token)).thenReturn(name);

        mockMvc.perform(post("/auth/public/findName")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(content))
                .andExpect(status().isOk())
                .andExpect(jsonPath("code").value(1000))
                .andExpect(jsonPath("result").value(name));
    }

    @Test
    void instropect_success() throws Exception {
        String token = "token123";
        TokenRequest request = TokenRequest.builder()
                .token(token)
                .build();
        String content = objectMapper.writeValueAsString(request);

        when(authService.instropect(request)).thenReturn(true);

        mockMvc.perform(post("/auth/instropect")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(content))
                .andExpect(status().isOk())
                .andExpect(jsonPath("code").value(1000))
                .andExpect(jsonPath("result").value(true));
    }

    @Test
    void logout_success() throws Exception {
        String token = "token123";
        TokenRequest request = TokenRequest.builder()
                .token(token)
                .build();
        String content = objectMapper.writeValueAsString(request);

        when(authService.logout(request)).thenReturn(true);

        mockMvc.perform(post("/auth/public/logout")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(content))
                .andExpect(status().isOk())
                .andExpect(jsonPath("code").value(1000))
                .andExpect(jsonPath("result").value(true));
    }

}
