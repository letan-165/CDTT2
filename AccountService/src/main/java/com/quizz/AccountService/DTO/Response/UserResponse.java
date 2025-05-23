package com.quizz.AccountService.DTO.Response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
    String userID;
    String name;
    String email;
    String phone;
    String role;
}
