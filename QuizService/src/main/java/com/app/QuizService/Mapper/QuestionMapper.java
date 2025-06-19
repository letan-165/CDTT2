package com.app.QuizService.Mapper;

import com.app.QuizService.DTO.BaseDTO.Question;
import com.app.QuizService.DTO.BaseDTO.QuestionEdit;
import com.app.QuizService.DTO.BaseDTO.QuestionSubmit;
import com.app.QuizService.DTO.Response.QuizDetail.QuestionResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface QuestionMapper {
    @Mapping(target = "questionID", ignore = true)
    Question toQuestion(QuestionEdit questionSave);

    QuestionSubmit toQuestionSubmit(Question question);
    Question toQuestion(QuestionSubmit question);
}
