package com.app.NotificationService.DTO.BaseDTO;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NotificationBase {
    int index;
    String subject;
    String content;
    Instant startTime;
    Instant endTime;
    Instant displayTime;
    boolean isRead;
}
