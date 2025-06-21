package com.app.QuizService.DTO.Request;

import com.app.QuizService.Validation.ValidQuizTime;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@ValidQuizTime
public class EditQuizRequest {
    String quizID;
    String teacherID;
    String title;
    List<String> topics;
    String description;
    Instant startTime;
    Instant endTime;
    long duration;
}
