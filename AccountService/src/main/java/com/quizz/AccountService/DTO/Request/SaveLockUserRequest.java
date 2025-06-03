package com.quizz.AccountService.DTO.Request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SaveLockUserRequest {
    String userID;
    int expiryTime;
}
