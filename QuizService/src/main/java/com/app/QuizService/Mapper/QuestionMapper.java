package com.app.QuizService.Mapper;

import com.app.QuizService.DTO.BaseDTO.Question;
import com.app.QuizService.DTO.BaseDTO.QuestionSave;
import com.app.QuizService.DTO.BaseDTO.QuestionSubmit;
import com.app.QuizService.DTO.Request.SubmitQuizRequest;
import com.app.QuizService.DTO.Response.QuizDetail.QuestionResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface QuestionMapper {
    QuestionResponse toQuestionResponse(Question question);
    Question toQuestion(QuestionSave questionSave);

    QuestionSubmit toQuestionSubmit(Question question);
    Question toQuestion(QuestionSubmit question);
}
