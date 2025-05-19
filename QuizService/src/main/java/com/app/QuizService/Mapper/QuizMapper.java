package com.app.QuizService.Mapper;

import com.app.QuizService.DTO.BaseDTO.Question;
import com.app.QuizService.DTO.BaseDTO.QuestionSave;
import com.app.QuizService.DTO.Request.SaveQuizRequest;
import com.app.QuizService.DTO.Response.QuizDetail.QuestionResponse;
import com.app.QuizService.DTO.Response.QuizDetail.QuizResponse;
import com.app.QuizService.Entity.Quiz;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Mapper(componentModel = "spring")
public abstract class QuizMapper {
    @Autowired
    private QuestionMapper questionMapper;

    @Mapping(target = "questions", source = "questions", qualifiedByName = "toListQuestions")
    public abstract QuizResponse toQuizResponse(Quiz quiz);

    @Mapping(target = "questions", source = "questions", qualifiedByName = "toMapQuestions")
    public abstract Quiz toQuiz(SaveQuizRequest request);

    @Named("toListQuestions")
    List<QuestionResponse> toListQuestions(Map<Integer, Question> questions){
        return questions.values().stream()
                .map(questionMapper::toQuestionResponse)
                .toList();
    }

    @Named("toMapQuestions")
    Map<Integer, Question> toMapQuestions(List<QuestionSave> questions){
        return IntStream.range(0, questions.size())
                .boxed()
                .collect(Collectors.toMap(i -> i, i -> {
                            Question question = questionMapper.toQuestion(questions.get(i));
                            question.setQuestionID(i);
                            return question;
                        }
                ));
    }
}
