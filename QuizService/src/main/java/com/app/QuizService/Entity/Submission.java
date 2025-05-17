package com.app.QuizService.Entity;

import com.app.QuizService.DTO.BaseDTO.Result;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Submission {
    @Id
    String studentID;
    List<Result> results;
}
