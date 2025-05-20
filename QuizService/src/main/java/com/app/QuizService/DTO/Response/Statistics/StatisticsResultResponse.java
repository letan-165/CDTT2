package com.app.QuizService.DTO.Response.Statistics;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StatisticsResultResponse {
    String resultID;
    String studentID;
    int totalCorrectAnswers;
    double score;
}
