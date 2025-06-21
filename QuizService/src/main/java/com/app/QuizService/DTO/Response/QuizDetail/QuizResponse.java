package com.app.QuizService.DTO.Response.QuizDetail;

import com.app.QuizService.DTO.BaseDTO.Question;
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
public class QuizResponse {
    String quizID;
    String teacherID;
    String teacherName;
    String title;
    List<String> topics;
    String description;
    List<Question> questions;
    Instant startTime;
    Instant endTime;
    long duration;
}
