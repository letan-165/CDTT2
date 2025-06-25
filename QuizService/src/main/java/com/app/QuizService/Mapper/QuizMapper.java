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

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Mapper(componentModel = "spring")
public abstract class QuizMapper {
    @Autowired
    private QuestionMapper questionMapper;

    @Mapping(target = "questions", source = "questions", qualifiedByName = "toListQuestions")
    @Mapping(target = "duration", source = "duration", qualifiedByName = "toMinute")
    @Mapping(target = "startTime", source = "startTime", qualifiedByName = "toVNTime")
    @Mapping(target = "endTime", source = "endTime", qualifiedByName = "toVNTime")
    public abstract QuizResponse toQuizResponse(Quiz quiz);

    @Mapping(target = "duration", source = "duration", qualifiedByName = "toDuration")
    public abstract Quiz toQuiz(EditQuizRequest request);

    public abstract SearchQuiz toSearchQuiz(Quiz quiz);

    @Named("toListQuestions")
    List<Question> toListQuestions(Map<Integer, Question> questions){
        return questions.values().stream().toList();
    }

    @Named("toDuration")
    Duration toDuration(long duration){
        return Duration.ofMinutes(duration);
    }

    @Named("toMinute")
    long toMinute(Duration duration){
        return duration.toMinutes();
    }


    @Named("toVNTime")
    String toVNTime(Instant time){
        if (time == null) return null;
        ZoneId zoneVN = ZoneId.of("Asia/Ho_Chi_Minh");
        LocalDateTime vnTime = LocalDateTime.ofInstant(time, zoneVN);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

        return vnTime.format(formatter);
    }
}
