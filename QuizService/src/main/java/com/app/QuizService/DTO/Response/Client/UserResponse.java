package com.app.QuizService.DTO.Response.Client;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
    String userID;
    String name;
    String phone;
    String gmail;
    String role;
}