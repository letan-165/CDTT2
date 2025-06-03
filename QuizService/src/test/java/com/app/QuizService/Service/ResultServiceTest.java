package com.app.QuizService.Service;


import quizz.library.common.DTO.ApiResponse;
import quizz.library.common.Exception.AppException;
import quizz.library.common.Exception.ErrorCode;
import com.app.QuizService.DTO.BaseDTO.Question;
import com.app.QuizService.DTO.BaseDTO.QuestionSubmit;
import com.app.QuizService.DTO.Request.JoinQuizRequest;
import com.app.QuizService.DTO.Request.SubmitQuizRequest;
import com.app.QuizService.DTO.Response.Client.UserResponse;
import com.app.QuizService.DTO.Response.TodoQuiz.ResultResponse;
import com.app.QuizService.DTO.Response.TodoQuiz.SubmitQuizResponse;
import com.app.QuizService.Entity.Quiz;
import com.app.QuizService.Entity.Result;
import com.app.QuizService.Mapper.QuestionMapper;
import com.app.QuizService.Mapper.ResultMapper;
import com.app.QuizService.Repository.HttpClient.NotificationClient;
import com.app.QuizService.Repository.HttpClient.UserClient;
import com.app.QuizService.Repository.MongoDB.QuizRepository;
import com.app.QuizService.Repository.MongoDB.ResultRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ResultServiceTest {
    @Spy
    @InjectMocks
    ResultService resultService;

    @Mock ResultRepository resultRepository;
    @Mock QuizRepository quizRepository;
    @Mock ResultMapper resultMapper;
    @Mock QuestionMapper questionMapper;
    @Mock UserClient userClient;
    @Mock NotificationClient notificationClient;

    @Mock Quiz quiz;
    @Mock Result result;
    @Mock Question question;
    @Mock SubmitQuizResponse submitQuizResponse;
    @Mock ResultResponse resultResponse;

    @BeforeEach
    void initData(){
        lenient().doReturn(submitQuizResponse).when(resultService).toSubmitQuizResponse(any());
    }

    @Test
    void join_success(){
        JoinQuizRequest request = JoinQuizRequest.builder()
                .quizID("quizID")
                .studentID("studentID")
                .build();
        UserResponse userResponse = mock(UserResponse.class);
        var apiResponse = mock(ApiResponse.class);

        when(userClient.findById(eq(request.getStudentID()))).thenReturn(apiResponse);
        when(apiResponse.getResult()).thenReturn(userResponse);

        when(quizRepository.findById(eq(request.getQuizID()))).thenReturn(Optional.of(quiz));
        when(resultRepository.save(any())).thenReturn(result);
        when(quiz.getStartTime()).thenReturn(Instant.now().plus(6, ChronoUnit.MINUTES));

        SubmitQuizResponse response = resultService.join(request);

        verify(notificationClient,times(2)).sendNotification(any());
        assertThat(response).isEqualTo(submitQuizResponse);
    }

    @Test
    void submit_success(){
        Map<Integer, Question> questionMap = mock(HashMap.class);
        QuestionSubmit questionSubmit = new QuestionSubmit();
        SubmitQuizRequest request = SubmitQuizRequest.builder()
                .resultID("resultID")
                .questions(List.of(questionSubmit,questionSubmit))
                .build();

        when(resultRepository.findById(eq(request.getResultID()))).thenReturn(Optional.of(result));
        when(result.getQuiz()).thenReturn(quiz);
        when(quiz.getQuestions()).thenReturn(questionMap);
        when(quiz.getStartTime()).thenReturn(Instant.now());
        when(questionMap.get(questionSubmit.getQuestionID())).thenReturn(question);

        SubmitQuizResponse response = resultService.submit(request);

        verify(result,times(5)).getQuiz();
        verify(question,times(2)).setAnswers(any());
        verify(quiz,times(4)).getQuestions();

        assertThat(response).isEqualTo(submitQuizResponse);
    }

    @Test
    void submit_fail_TIME_TODO_INVALID(){
        Map<Integer, Question> questionMap = mock(HashMap.class);
        QuestionSubmit questionSubmit = new QuestionSubmit();
        SubmitQuizRequest request = SubmitQuizRequest.builder()
                .resultID("resultID")
                .questions(List.of(questionSubmit,questionSubmit))
                .build();

        when(resultRepository.findById(eq(request.getResultID()))).thenReturn(Optional.of(result));
        when(result.getQuiz()).thenReturn(quiz);
        when(quiz.getStartTime()).thenReturn(Instant.now().plus(10,ChronoUnit.MINUTES));

        var exception = assertThrows(AppException.class,
                ()->resultService.submit(request));

        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.TIME_TODO_INVALID);
    }

    @Test
    void finish_success(){
        result = Result.builder().quiz(quiz).build();
        String resultID ="resultID";
        when(resultRepository.findById(eq(resultID))).thenReturn(Optional.of(result));

        when(quiz.getQuestions()).thenReturn(new HashMap<>(Map.of(1,question,2,question)));

        when(question.checkAnswer()).thenReturn(true);
        when(resultRepository.save(any())).thenReturn(result);
        when(resultMapper.toResultResponse(result)).thenReturn(resultResponse);

        ResultResponse response = resultService.finish(resultID);

        verify(question,times(2)).checkAnswer();
        assertThat(response).isEqualTo(resultResponse);
    }

    @Test
    void statisticStudentResultTimes_success(){
        String studentID = "studentID";
        List<Result> results = mockResults();

        when(resultRepository.findAllByStudentID(eq(studentID))).thenReturn(results);

        List<Integer> response = resultService.statisticStudentResultTimes(studentID,0);

        assertThat(response.size()).isEqualTo(7);
        assertThat(response.get(0)).isEqualTo(60);
        assertThat(response.get(1)).isEqualTo(0);
        assertThat(response.get(2)).isEqualTo(180);
        assertThat(response.get(3)).isEqualTo(0);
        assertThat(response.get(4)).isEqualTo(180);
        assertThat(response.get(5)).isEqualTo(0);
        assertThat(response.get(6)).isEqualTo(0);
    }

    List<Result> mockResults(){
        List<Result> results = new ArrayList<>();
        ZonedDateTime monday = Instant.now()
                .atZone(ZoneId.systemDefault())
                .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
                .minusWeeks(0)
                .with(LocalTime.MIDNIGHT);

        results.add(Result.builder()
                .startTime(monday.plusDays(0).withHour(1).toInstant())
                .endTime(monday.plusDays(0).withHour(2).toInstant())
                .build());

        results.add(Result.builder()
                .startTime(monday.plusDays(2).withHour(1).toInstant())
                .endTime(monday.plusDays(2).withHour(3).toInstant())
                .build());

        results.add(Result.builder()
                .startTime(monday.plusDays(2).withHour(4).toInstant())
                .endTime(monday.plusDays(2).withHour(5).toInstant())
                .build());

        results.add(Result.builder()
                .startTime(monday.plusDays(4).withHour(1).toInstant())
                .endTime(monday.plusDays(4).withHour(4).toInstant())
                .build());

        return results;
    }


}
