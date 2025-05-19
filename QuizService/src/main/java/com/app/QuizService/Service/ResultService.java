package com.app.QuizService.Service;

import com.app.QuizService.DTO.BaseDTO.Question;
import com.app.QuizService.DTO.BaseDTO.QuestionSubmit;
import com.app.QuizService.DTO.Request.JoinQuizRequest;
import com.app.QuizService.DTO.Request.SaveQuizRequest;
import com.app.QuizService.DTO.Request.SubmitQuizRequest;
import com.app.QuizService.DTO.Response.Statistics.StatisticsResponse;
import com.app.QuizService.DTO.Response.TodoQuiz.ResultResponse;
import com.app.QuizService.DTO.Response.TodoQuiz.SubmitQuizResponse;
import com.app.QuizService.Entity.Quiz;
import com.app.QuizService.Entity.Result;
import com.app.QuizService.Mapper.QuestionMapper;
import com.app.QuizService.Mapper.ResultMapper;
import com.app.QuizService.Repository.HttpClient.UserClient;
import com.app.QuizService.Repository.QuizRepository;
import com.app.QuizService.Repository.ResultRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class ResultService {
    ResultRepository resultRepository;
    QuizRepository quizRepository;
    ResultMapper resultMapper;
    QuestionMapper questionMapper;
    UserClient userClient;

    SubmitQuizResponse toSubmitQuizResponse(Result result){
        var questions = result.getQuiz().getQuestions().values().stream()
                .map(questionMapper::toQuestionSubmit)
                .toList();

        return SubmitQuizResponse.builder()
                .resultID(result.getResultID())
                .questions(questions)
                .build();
    }

    public SubmitQuizResponse join(JoinQuizRequest request){
        userClient.findById(request.getStudentID());
        Quiz quiz = quizRepository.findById(request.getQuizID())
                .orElseThrow(()-> new RuntimeException("Lỗi bài quiz không tồn tại trong hệ thống"));

        Result result = Result.builder()
                .quiz(quiz)
                .studentID(request.getStudentID())
                .score(0)
                .startTime(Instant.now())
                .build();

        Result response = resultRepository.save(result);
        return toSubmitQuizResponse(response);
    }

    public SubmitQuizResponse submit(SubmitQuizRequest request){
        Result result = resultRepository.findById(request.getResultID())
                .orElseThrow(()->new RuntimeException("Lỗi kết quả chưa được khởi tạo trong hệ thống"));

        request.getQuestions().forEach(questionRequest ->{
            Quiz quiz = result.getQuiz();
            //Give question from ID request
            Question question = quiz.getQuestions().get(questionRequest.getQuestionID());
            var answers = questionRequest.getAnswer();
            answers.forEach(answer-> {
                if(answer >= quiz.getQuestions().size() || answer < 0)
                    throw new RuntimeException("Lỗi thứ tự đáp án không hợp lệ");
            });
            //Update answer
            question.setAnswer(answers);
            result.getQuiz().getQuestions()
                    .put(questionRequest.getQuestionID(),question);
        });

        return toSubmitQuizResponse(resultRepository.save(result));
    }

    boolean checkResult(Question question){
        var answers = question.getAnswer();
        var corrects = question.getCorrects();
        if(answers.size() != corrects.size())
            return false;

        return new HashSet<>(corrects).containsAll(answers);
    }

    public ResultResponse finish(String resultID){
        Result result = resultRepository.findById(resultID)
                .orElseThrow(()->new RuntimeException("Lỗi kết quả chưa được khởi tạo trong hệ thống"));

        int totalCorrectAnswers = (int) result.getQuiz().getQuestions().values().stream()
                .filter(this::checkResult)
                .count();
        double score = (double) 10 /result.getQuiz().getQuestions().size() * totalCorrectAnswers;

        Result response = resultRepository.save(result.toBuilder()
                        .endTime(Instant.now())
                        .totalCorrectAnswers(totalCorrectAnswers)
                        .score(score)
                .build());

        return resultMapper.toResultResponse(response);
    }

    public List<ResultResponse> findByID(String quizID, String studentID){
        return new ArrayList<>();
    }

    public StatisticsResponse statistic(String quizID){
        return new StatisticsResponse();
    }



}
