package com.app.NotificationService.DTO.Request.Email;
import com.app.NotificationService.DTO.BaseDTO.Sender;
import lombok.*;
import lombok.experimental.FieldDefaults;


@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SendEmailRequest {
    Sender to;
    String subject;
    String content;
}
