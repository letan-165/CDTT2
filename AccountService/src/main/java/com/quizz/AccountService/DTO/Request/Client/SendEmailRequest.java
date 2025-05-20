package com.quizz.AccountService.DTO.Request.Client;
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
