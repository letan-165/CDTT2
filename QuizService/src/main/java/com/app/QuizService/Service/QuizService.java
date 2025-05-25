package com.app.QuizService.Service;

import com.app.QuizService.DTO.Request.SaveQuizRequest;
import com.app.QuizService.DTO.Response.QuizDetail.QuizResponse;
import com.app.QuizService.Entity.Elastic.SearchQuiz;
import com.app.QuizService.Entity.Quiz;
import com.app.QuizService.Exception.AppException;
import com.app.QuizService.Exception.ErrorCode;
import com.app.QuizService.Mapper.QuestionMapper;
import com.app.QuizService.Mapper.QuizMapper;
import com.app.QuizService.Repository.HttpClient.UserClient;
import com.app.QuizService.Repository.QuizRepository;
import com.app.QuizService.Repository.SearchQuizRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.Named;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class QuizService {
    QuizRepository quizRepository;
    SearchQuizRepository searchQuizRepository;
    QuizMapper quizMapper;
    QuestionMapper questionMapper;
    UserClient userClient;

    public QuizResponse save(SaveQuizRequest request){
        try{
            userClient.findById(request.getTeacherID());
        } catch (Exception e) {
            throw new AppException(ErrorCode.USER_NO_EXIST);
        }
        request.getQuestions().forEach(questionSave -> {
            int size = questionSave.getOptions().size();
            questionSave.getCorrects().forEach(correct -> {
                if(correct >= size || correct < 0)
                    throw new AppException(ErrorCode.CORRECT_INVALID);
            });
        });
        Quiz response = quizRepository.save(quizMapper.toQuiz(request));

        searchQuizRepository.save(quizMapper.toSearchQuiz(response));

        return quizMapper.toQuizResponse(response);
    }

    public List<SearchQuiz> searchTagTitle(String title){
        return searchQuizRepository.findByTitleContainingOrTopicsContaining(title,title);
    }

    public List<String> searchTagTopic(String topic){
        var searchQuizs = searchQuizRepository.findByTopicsContaining(topic);
        return searchQuizs.stream()
                .sorted(Comparator.comparing(SearchQuiz::getStartTime))
                .flatMap(searchQuiz -> searchQuiz.getTopics().stream())
                .toList();
    }
}
