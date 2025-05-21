package com.quizz.AccountService.DTO.Request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserSignUpRequest {
    String name;
    String password;
    String email;
    String phone;
    int otp;
    String role;
}
