package com.app.QuizService.Service;

import com.app.QuizService.DTO.BaseDTO.Question;
import com.app.QuizService.DTO.BaseDTO.QuestionSave;
import com.app.QuizService.DTO.Request.SaveQuizRequest;
import com.app.QuizService.DTO.Response.QuizDetail.QuizResponse;
import com.app.QuizService.Entity.Quiz;
import com.app.QuizService.Mapper.QuestionMapper;
import com.app.QuizService.Mapper.QuizMapper;
import com.app.QuizService.Repository.HttpClient.UserClient;
import com.app.QuizService.Repository.QuizRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.Named;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class QuizService {
    QuizRepository quizRepository;
    QuizMapper quizMapper;
    QuestionMapper questionMapper;
    UserClient userClient;

    public QuizResponse save(SaveQuizRequest request){
        userClient.findById(request.getTeacherID());
        request.getQuestions().forEach(questionSave -> {
            int size = questionSave.getOptions().size();
            questionSave.getCorrects().forEach(correct -> {
                if(correct >= size || correct < 0)
                    throw new RuntimeException("Thứ tự câu đúng không hợp lệ");
            });
        });

        return quizMapper.toQuizResponse(
                quizRepository.save(quizMapper.toQuiz(request)));
    }

    public List<QuizResponse> statisticsTopic(String teacherID, String topic){
        userClient.findById(teacherID);
        var quizzes = quizRepository.findByTeacherIDAndTopic(teacherID,topic);
        return quizzes.stream()
                .map(quizMapper::toQuizResponse)
                .toList();
    }
}
