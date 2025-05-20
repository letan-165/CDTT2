package com.app.NotificationService.Service;

import com.app.NotificationService.DTO.BaseDTO.Sender;
import com.app.NotificationService.DTO.Request.Email.EmailFullRequest;
import com.app.NotificationService.DTO.Request.Email.SendEmailRequest;
import com.app.NotificationService.DTO.Response.Email.EmailResponse;
import com.app.NotificationService.Repository.HttpClient.EmailClient;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class EmailService {
    EmailClient emailClient;

    @NonFinal
    @Value("${app.client.contact.email}")
    String emailContact;

    @NonFinal
    @Value("${app.client.contact.name}")
    String nameEmail;

    @NonFinal
    @Value("${key.email}")
    String keyEmail;

    public EmailResponse sendEmail(SendEmailRequest request){
        EmailFullRequest emailFullRequest = EmailFullRequest.builder()
                .sender(Sender.builder()
                        .email(emailContact)
                        .name(nameEmail)
                        .build())
                .to(new ArrayList<>(List.of(request.getTo())))
                .htmlContent("<h3>Quiz chào bạn,</h2></br><p>"+ request.getContent() +"</p></br><h4>Quiz xin cám ơn!</h4>")
                .subject(request.getSubject())
                .build();

        return emailClient.sendEmail(keyEmail,emailFullRequest);
    }
}
