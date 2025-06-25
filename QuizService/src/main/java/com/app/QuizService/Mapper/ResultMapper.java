package com.app.QuizService.Mapper;

import com.app.QuizService.DTO.BaseDTO.Question;
import com.app.QuizService.DTO.Response.QuizDetail.QuestionResponse;
import com.app.QuizService.DTO.Response.QuizDetail.QuizResponse;
import com.app.QuizService.DTO.Response.Statistics.StatisticsResultResponse;
import com.app.QuizService.DTO.Response.TodoQuiz.ResultResponse;
import com.app.QuizService.Entity.Quiz;
import com.app.QuizService.Entity.Result;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Mapper(componentModel = "spring")
public abstract class ResultMapper {
    @Autowired
    private QuizMapper  quizMapper;

    @Mapping(target = "quiz", source = "quiz", qualifiedByName = "toQuizResponse")
    @Mapping(target = "startTime", source = "startTime", qualifiedByName = "toVNTime")
    @Mapping(target = "endTime", source = "endTime", qualifiedByName = "toVNTime")
    public abstract ResultResponse toResultResponse(Result result);

    public abstract StatisticsResultResponse toStatisticsResultResponse(Result result);

    @Named("toQuizResponse")
    QuizResponse toQuizResponse(Quiz quiz){
        return quizMapper.toQuizResponse(quiz);
    }

    @Named("toVNTime")
    String toVNTime(Instant time){
        if (time == null) return null;
        ZoneId zoneVN = ZoneId.of("Asia/Ho_Chi_Minh");
        LocalDateTime vnTime = LocalDateTime.ofInstant(time, zoneVN);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

        return vnTime.format(formatter);
    }
}
