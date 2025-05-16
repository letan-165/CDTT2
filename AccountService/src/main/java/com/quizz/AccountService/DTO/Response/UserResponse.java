package com.quizz.AccountService.DTO.Response;

import com.quizz.AccountService.Entity.Role;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
    String userID;
    String name;
    String gmail;
    String phone;
    List<String> roles;
}
