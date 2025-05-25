package com.app.NotificationService.DTO.Response;

import com.app.NotificationService.DTO.BaseDTO.NotificationBase;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PersonalResponse {
    String userID;
    List<NotificationResponse> notifications;
}
