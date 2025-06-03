package com.app.NotificationService.DTO.Request.Email;

import com.app.NotificationService.DTO.BaseDTO.Sender;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Builder(toBuilder = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EmailFullRequest {
    Sender sender;
    List<Sender> to;
    String htmlContent;
    String subject;
}
