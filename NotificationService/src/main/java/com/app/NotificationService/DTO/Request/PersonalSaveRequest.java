package com.app.NotificationService.DTO.Request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PersonalSaveRequest {
    String userID;
    String subject;
    String content;
    Instant displayTime;
}
