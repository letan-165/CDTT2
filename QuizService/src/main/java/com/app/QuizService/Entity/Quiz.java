package com.app.QuizService.Entity;

import com.app.QuizService.DTO.BaseDTO.Question;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Document
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Quiz {
    @Id
    String quizID;
    String teacherID;
    String teacherName;
    String title;
    List<String> topics;
    String description;

    @Builder.Default
    Map<Integer,Question> questions = new HashMap<>() ;
    Instant startTime;
    Instant endTime;
    Duration duration;
}
