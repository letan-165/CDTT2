package com.app.QuizService.DTO.Request.Client;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SendNotificationRequest {
    String name;
    String subject;
    String content;
    Instant displayTime;
}
