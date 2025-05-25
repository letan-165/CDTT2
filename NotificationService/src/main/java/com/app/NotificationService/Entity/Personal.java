package com.app.NotificationService.Entity;

import com.app.NotificationService.DTO.BaseDTO.NotificationBase;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(value = "Personal-Notification")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Personal {
    @Id
    String userID;
    List<NotificationBase> notifications;
}
