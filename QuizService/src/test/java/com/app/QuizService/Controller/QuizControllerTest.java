package com.app.QuizService.Controller;


import com.app.QuizService.DTO.BaseDTO.Question;
import com.app.QuizService.DTO.Request.EditQuizRequest;
import com.app.QuizService.DTO.Request.QuestionDelRequest;
import com.app.QuizService.DTO.Request.QuestionEditRequest;
import com.app.QuizService.DTO.Request.SearchRequest;
import com.app.QuizService.DTO.Response.QuizDetail.QuestionResponse;
import com.app.QuizService.DTO.Response.QuizDetail.QuizResponse;
import com.app.QuizService.Entity.Elastic.SearchQuiz;
import com.app.QuizService.Service.QuizService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import quizz.library.common.Exception.ErrorCode;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class QuizControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    QuizService quizService;

    QuizResponse quizResponse;
    @BeforeEach
    void initData(){
        quizResponse = QuizResponse.builder()
                .quizID("quizID")
                .teacherID("tanID")
                .title("title")
                .topics(List.of("T1","T2"))
                .description("description")
                .questions(List.of( new Question(),new Question()))
                .startTime(Instant.parse("2025-05-31T11:00:00.00Z"))
                .endTime(Instant.parse("2025-05-31T12:00:00.00Z"))
                .duration(20)
                .build();
    }

    @Test
    void findAllByTeacher_success() throws Exception{
        String teacherID = quizResponse.getTeacherID();
        var list = List.of(quizResponse,quizResponse);

        when(quizService.findAllByTeacherID(teacherID)).thenReturn(list);

        mockMvc.perform(get("/quiz/public/teacher/"+teacherID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("code").value(1000))
                .andExpect(jsonPath("result[0].quizID").value(quizResponse.getQuizID()))
                .andExpect(jsonPath("result[0].teacherID").value(quizResponse.getTeacherID()))
                .andExpect(jsonPath("result[0].title").value(quizResponse.getTitle()))
                .andExpect(jsonPath("result[0].topics.length()").value(2))
                .andExpect(jsonPath("result[0].description").value(quizResponse.getDescription()))
                .andExpect(jsonPath("result[0].questions.length()").value(2))
                .andExpect(jsonPath("result[0].startTime").value(quizResponse.getStartTime().toString()))
                .andExpect(jsonPath("result[0].endTime").value(quizResponse.getEndTime().toString()))
                .andExpect(jsonPath("result[0].duration").value(quizResponse.getDuration()));

    }

    @Test
    void save_success() throws Exception{
        String teacherID = quizResponse.getTeacherID();
        EditQuizRequest request = EditQuizRequest.builder()
                .startTime(Instant.parse("2025-05-31T11:00:00.00Z"))
                .endTime(Instant.parse("2025-05-31T12:00:00.00Z"))
                .duration(20)
                .build();
        String content = objectMapper.writeValueAsString(request);
        when(quizService.save(request)).thenReturn(quizResponse);

        mockMvc.perform(post("/quiz/public")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content))
                .andExpect(status().isOk())
                .andExpect(jsonPath("code").value(1000))
                .andExpect(jsonPath("result.quizID").value(quizResponse.getQuizID()))
                .andExpect(jsonPath("result.teacherID").value(quizResponse.getTeacherID()))
                .andExpect(jsonPath("result.title").value(quizResponse.getTitle()))
                .andExpect(jsonPath("result.topics.length()").value(2))
                .andExpect(jsonPath("result.description").value(quizResponse.getDescription()))
                .andExpect(jsonPath("result.questions.length()").value(2))
                .andExpect(jsonPath("result.startTime").value(quizResponse.getStartTime().toString()))
                .andExpect(jsonPath("result.endTime").value(quizResponse.getEndTime().toString()))
                .andExpect(jsonPath("result.duration").value(quizResponse.getDuration()));

    }

    @Test
    void save_fail_FIELD_TIME_NOTNULL() throws Exception{
        String teacherID = quizResponse.getTeacherID();
        ErrorCode errorCode = ErrorCode.FIELD_TIME_NOTNULL;
        EditQuizRequest request = EditQuizRequest.builder()
                .endTime(Instant.parse("2025-05-31T12:00:00.00Z"))
                .duration(20)
                .build();
        String content = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/quiz/public")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("code").value(errorCode.getCode()))
                .andExpect(jsonPath("message").value(errorCode.getMessage()));

    }

    @Test
    void save_fail_BETWEEN_TIME_INVALID() throws Exception{
        String teacherID = quizResponse.getTeacherID();
        ErrorCode errorCode = ErrorCode.BETWEEN_TIME_INVALID;
        EditQuizRequest request = EditQuizRequest.builder()
                .startTime(Instant.parse("2025-05-31T12:00:00.00Z"))
                .endTime(Instant.parse("2025-05-31T11:00:00.00Z"))
                .duration(20)
                .build();
        String content = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/quiz/public")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("code").value(errorCode.getCode()))
                .andExpect(jsonPath("message").value(errorCode.getMessage()));

    }

    @Test
    void save_fail_DURATION_FIELD_INVALID() throws Exception{
        String teacherID = quizResponse.getTeacherID();
        ErrorCode errorCode = ErrorCode.BETWEEN_TIME_INVALID;
        EditQuizRequest request = EditQuizRequest.builder()
                .startTime(Instant.parse("2025-05-31T12:00:00.00Z"))
                .endTime(Instant.parse("2025-05-31T11:00:00.00Z"))
                .duration(20)
                .build();
        String content = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/quiz/public")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("code").value(errorCode.getCode()))
                .andExpect(jsonPath("message").value(errorCode.getMessage()));

    }

    @Test
    void saveQuestion_success() throws Exception{
        QuestionEditRequest request = new QuestionEditRequest();

        String content = objectMapper.writeValueAsString(request);
        when(quizService.saveQuestion(request)).thenReturn(quizResponse);

        mockMvc.perform(put("/quiz/public/question")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content))
                .andExpect(status().isOk())
                .andExpect(jsonPath("code").value(1000))
                .andExpect(jsonPath("result.quizID").value(quizResponse.getQuizID()))
                .andExpect(jsonPath("result.teacherID").value(quizResponse.getTeacherID()))
                .andExpect(jsonPath("result.title").value(quizResponse.getTitle()))
                .andExpect(jsonPath("result.topics.length()").value(2))
                .andExpect(jsonPath("result.description").value(quizResponse.getDescription()))
                .andExpect(jsonPath("result.questions.length()").value(2))
                .andExpect(jsonPath("result.startTime").value(quizResponse.getStartTime().toString()))
                .andExpect(jsonPath("result.endTime").value(quizResponse.getEndTime().toString()))
                .andExpect(jsonPath("result.duration").value(quizResponse.getDuration()));

    }

    @Test
    void deleteQuestion_success() throws Exception{
        QuestionDelRequest request = new QuestionDelRequest();

        String content = objectMapper.writeValueAsString(request);
        when(quizService.deleteQuestion(request)).thenReturn(quizResponse);

        mockMvc.perform(put("/quiz/public/question/delete")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content))
                .andExpect(status().isOk())
                .andExpect(jsonPath("code").value(1000))
                .andExpect(jsonPath("result.quizID").value(quizResponse.getQuizID()))
                .andExpect(jsonPath("result.teacherID").value(quizResponse.getTeacherID()))
                .andExpect(jsonPath("result.title").value(quizResponse.getTitle()))
                .andExpect(jsonPath("result.topics.length()").value(2))
                .andExpect(jsonPath("result.description").value(quizResponse.getDescription()))
                .andExpect(jsonPath("result.questions.length()").value(2))
                .andExpect(jsonPath("result.startTime").value(quizResponse.getStartTime().toString()))
                .andExpect(jsonPath("result.endTime").value(quizResponse.getEndTime().toString()))
                .andExpect(jsonPath("result.duration").value(quizResponse.getDuration()));

    }

    @Test
    void statisticsTitle_success() throws Exception{
        String search = "hello";
        SearchRequest request = SearchRequest.builder().search(search).build();
        List<SearchQuiz> list = List.of(new SearchQuiz(),new SearchQuiz());
        String content = objectMapper.writeValueAsString(request);

        when(quizService.searchTagTitle(search)).thenReturn(list);

        mockMvc.perform(post("/quiz/public/title/search")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content))
                .andExpect(status().isOk())
                .andExpect(jsonPath("code").value(1000))
                .andExpect(jsonPath("result.length()").value(2));

    }

    @Test
    void statisticsTopic_success() throws Exception{
        String search = "hello";
        SearchRequest request = SearchRequest.builder().search(search).build();
        Set<String> set = Set.of("hello1","hello2");
        String content = objectMapper.writeValueAsString(request);

        when(quizService.searchTagTopic(search)).thenReturn(set);

        mockMvc.perform(post("/quiz/public/topic/search")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content))
                .andExpect(status().isOk())
                .andExpect(jsonPath("code").value(1000))
                .andExpect(jsonPath("result.length()").value(2))
                .andExpect(jsonPath("result[0]").value("hello1"))
                .andExpect(jsonPath("result[1]").value("hello2"));

    }


}
