package com.app.QuizService.Mapper;

import com.app.QuizService.DTO.Response.Statistics.StatisticsResultResponse;
import com.app.QuizService.DTO.Response.TodoQuiz.ResultResponse;
import com.app.QuizService.Entity.Result;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ResultMapper {
    ResultResponse toResultResponse(Result result);

    StatisticsResultResponse toStatisticsResultResponse(Result result);
}
