package com.quizz.AccountService.DTO.Request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ForgotPassRequest {
    String username;
    String password;
    String email;
    int otp;
}
