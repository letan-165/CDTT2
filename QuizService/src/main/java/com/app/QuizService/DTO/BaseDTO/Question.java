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
    boolean isCorrect;

    public boolean checkQuestion(){
        if(type.equals("ENTER")){
            int count = content.split("=@=", -1).length - 1;
            return count == corrects.size();
        } else {
            return new HashSet<>(options).containsAll(corrects);
        }
    }

    public void checkAnswer(){
        if(answers.size()!=corrects.size()){
            isCorrect = false;
            return ;
        }

        if(type.equals("ENTER")){
            isCorrect = corrects.equals(answers);
        }else{
            isCorrect = new HashSet<>(answers).equals(new HashSet<>(corrects));
        }
    }


}
