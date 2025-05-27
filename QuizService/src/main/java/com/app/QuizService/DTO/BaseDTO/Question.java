package com.app.QuizService.DTO.BaseDTO;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Question {
    int questionID;
    String content;
    String type;
    List<String> options;
    List<String> corrects;
    List<String> answers;

    public boolean checkSelect(List<String> check){
        return new HashSet<>(options).containsAll(check);
    }

    public boolean checkEnter(List<String> check){
        int count = content.split("=@=", -1).length - 1;
        return count==check.size();
    }

    public boolean checkAnswer(){
        if(answers.size()!=corrects.size())
            return false;

        return new HashSet<>(corrects).containsAll(answers);
    }


}
