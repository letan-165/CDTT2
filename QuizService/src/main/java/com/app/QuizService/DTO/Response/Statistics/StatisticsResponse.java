package com.app.QuizService.DTO.Response.Statistics;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StatisticsResponse {
    String quizID;
    List<StatisticsResultResponse>results;
}
