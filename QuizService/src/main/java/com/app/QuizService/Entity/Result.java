package com.app.QuizService.Entity;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document
@Builder(toBuilder = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Result {
    @Id
    String resultID;
    Quiz quiz;
    String studentID;
    int totalCorrectAnswers;
    double score;
    Instant startTime;
    Instant endTime;
}
