package com.app.NotificationService.DTO.Response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NotificationResponse {
    int index;
    String subject;
    String content;
    String displayTime;
    boolean isRead;
}
