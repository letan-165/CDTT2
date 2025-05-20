package com.app.NotificationService.DTO.BaseDTO;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Sender {
    String name;
    String email;
}
