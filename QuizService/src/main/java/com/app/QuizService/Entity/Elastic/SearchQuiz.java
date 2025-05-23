package com.app.QuizService.Entity.Elastic;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.List;

@Document(indexName = "quiz_index")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SearchQuiz {
    @Id
    String quizID;
    String title;
    List<String> topics;
    String startTime;
}
