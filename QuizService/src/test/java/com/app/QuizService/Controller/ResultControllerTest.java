package com.app.QuizService.Controller;

import com.app.QuizService.DTO.BaseDTO.QuestionSubmit;
import com.app.QuizService.DTO.Request.JoinQuizRequest;
import com.app.QuizService.DTO.Request.SubmitQuizRequest;
import com.app.QuizService.DTO.Response.Statistics.StatisticsResponse;
import com.app.QuizService.DTO.Response.Statistics.StatisticsResultResponse;
import com.app.QuizService.DTO.Response.TodoQuiz.ResultResponse;
import com.app.QuizService.DTO.Response.TodoQuiz.SubmitQuizResponse;
import com.app.QuizService.Entity.Quiz;
import com.app.QuizService.Service.ResultService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ResultControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    ResultService resultService;
    SubmitQuizResponse submitQuizResponse;
    ResultResponse resultResponse;

    @BeforeEach
    void initData(){
        submitQuizResponse = SubmitQuizResponse.builder()
                .resultID("resultID")
                .questions(List.of(new QuestionSubmit(),new QuestionSubmit()))
                .build();

        resultResponse = ResultResponse.builder()
                .resultID("resultID")
                .quiz(new Quiz())
                .studentID("studentID")
                .totalCorrectAnswers(10)
                .score(9.5)
                .startTime(Instant.parse("2025-05-31T11:00:00.00Z"))
                .endTime(Instant.parse("2025-05-31T12:00:00.00Z"))
                .build();
    }

    @Test
    void join_success()throws Exception{
        JoinQuizRequest request = JoinQuizRequest.builder()
                .studentID("tanID")
                .quizID("quizID")
                .build();
        String content = objectMapper.writeValueAsString(request);
        when(resultService.join(request)).thenReturn(submitQuizResponse);

        mockMvc.perform(post("/result/public/join").content(content)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(content))
                .andExpect(status().isOk())
                .andExpect(jsonPath("code").value(1000))
                .andExpect(jsonPath("result.resultID").value(submitQuizResponse.getResultID()))
                .andExpect(jsonPath("result.questions.length()").value(2));

    }

    @Test
    void submit_success()throws Exception{
        SubmitQuizRequest request = SubmitQuizRequest.builder()
                .resultID("resultID")
                .questions(List.of(new QuestionSubmit(),new QuestionSubmit()))
                .build();
        String content = objectMapper.writeValueAsString(request);
        when(resultService.submit(request)).thenReturn(submitQuizResponse);

        mockMvc.perform(put("/result/public/submit").content(content)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(content))
                .andExpect(status().isOk())
                .andExpect(jsonPath("code").value(1000))
                .andExpect(jsonPath("result.resultID").value(submitQuizResponse.getResultID()))
                .andExpect(jsonPath("result.questions.length()").value(2));

    }

    @Test
    void finish_success()throws Exception{
        String resultID = "resultID";

        when(resultService.finish(resultID)).thenReturn(resultResponse);

        mockMvc.perform(put("/result/public/"+resultID+"/finish"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("code").value(1000))
                .andExpect(jsonPath("result.resultID").value(resultResponse.getResultID()))
                .andExpect(jsonPath("result.quiz").isNotEmpty())
                .andExpect(jsonPath("result.studentID").value(resultResponse.getStudentID()))
                .andExpect(jsonPath("result.totalCorrectAnswers").value(resultResponse.getTotalCorrectAnswers()))
                .andExpect(jsonPath("result.score").value(resultResponse.getScore()))
                .andExpect(jsonPath("result.startTime").value(resultResponse.getStartTime().toString()))
                .andExpect(jsonPath("result.endTime").value(resultResponse.getEndTime().toString()));
    }
    @Test
    void findByID_success()throws Exception{
        String resultID = "resultID";

        when(resultService.findByID(resultID)).thenReturn(resultResponse);

        mockMvc.perform(get("/result/public/"+resultID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("code").value(1000))
                .andExpect(jsonPath("result.resultID").value(resultResponse.getResultID()))
                .andExpect(jsonPath("result.quiz").isNotEmpty())
                .andExpect(jsonPath("result.studentID").value(resultResponse.getStudentID()))
                .andExpect(jsonPath("result.totalCorrectAnswers").value(resultResponse.getTotalCorrectAnswers()))
                .andExpect(jsonPath("result.score").value(resultResponse.getScore()))
                .andExpect(jsonPath("result.startTime").value(resultResponse.getStartTime().toString()))
                .andExpect(jsonPath("result.endTime").value(resultResponse.getEndTime().toString()));
    }

    @Test
    void statistics_success()throws Exception{
        String quizID ="quizID";
        StatisticsResponse response = StatisticsResponse.builder()
                .quizID(quizID)
                .results(List.of(new StatisticsResultResponse(), new StatisticsResultResponse()))
                .build();
        when(resultService.statistic(quizID)).thenReturn(response);

        mockMvc.perform(get("/result/public/quiz/"+quizID+"/statistics"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("code").value(1000))
                .andExpect(jsonPath("result.quizID").value(response.getQuizID()))
                .andExpect(jsonPath("result.results.length()").value(2));

    }

    @Test
    void statisticStudents_success()throws Exception{
        String studentID ="studentID";
        when(resultService.statisticStudents(studentID)).thenReturn(List.of(resultResponse,resultResponse));

        mockMvc.perform(get("/result/public/student/"+studentID+"/statistics"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("code").value(1000))
                .andExpect(jsonPath("result.length()").value(2))
                .andExpect(jsonPath("result[0].resultID").value(resultResponse.getResultID()))
                .andExpect(jsonPath("result[0].quiz").isNotEmpty())
                .andExpect(jsonPath("result[0].studentID").value(resultResponse.getStudentID()))
                .andExpect(jsonPath("result[0].totalCorrectAnswers").value(resultResponse.getTotalCorrectAnswers()))
                .andExpect(jsonPath("result[0].score").value(resultResponse.getScore()))
                .andExpect(jsonPath("result[0].startTime").value(resultResponse.getStartTime().toString()))
                .andExpect(jsonPath("result[0].endTime").value(resultResponse.getEndTime().toString()));

    }

    @Test
    void statisticStudentResultTimes_success()throws Exception{
        String studentID ="studentID";
        int lastWeek = 0;
        when(resultService.statisticStudentResultTimes(studentID,lastWeek)).thenReturn(List.of(0,1,2,3,4,5,6));

        mockMvc.perform(get("/result/public/student/"+studentID+"/week/"+lastWeek+"/statistics"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("code").value(1000))
                .andExpect(jsonPath("result.length()").value(7))
                .andExpect(jsonPath("result[0]").value(0))
                .andExpect(jsonPath("result[1]").value(1))
                .andExpect(jsonPath("result[2]").value(2))
                .andExpect(jsonPath("result[3]").value(3))
                .andExpect(jsonPath("result[4]").value(4))
                .andExpect(jsonPath("result[5]").value(5))
                .andExpect(jsonPath("result[6]").value(6));

    }



}
