package com.app.QuizService.Mapper;

import com.app.QuizService.DTO.BaseDTO.Question;
import com.app.QuizService.DTO.Request.EditQuizRequest;
import com.app.QuizService.DTO.Response.QuizDetail.QuestionResponse;
import com.app.QuizService.DTO.Response.QuizDetail.QuizResponse;
import com.app.QuizService.Entity.Elastic.SearchQuiz;
import com.app.QuizService.Entity.Quiz;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

@Mapper(componentModel = "spring")
public abstract class QuizMapper {
    @Autowired
    private QuestionMapper questionMapper;

    @Mapping(target = "questions", source = "questions", qualifiedByName = "toListQuestions")
    public abstract QuizResponse toQuizResponse(Quiz quiz);

    public abstract Quiz toQuiz(EditQuizRequest request);

    public abstract SearchQuiz toSearchQuiz(Quiz quiz);

    @Named("toListQuestions")
    List<Question> toListQuestions(Map<Integer, Question> questions){
        return questions.values().stream().toList();
    }

}
