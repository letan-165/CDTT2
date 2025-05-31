package com.app.QuizService.Service;

import co.elastic.clients.elasticsearch.core.SearchResponse;
import com.app.CommonLibrary.Exception.AppException;
import com.app.CommonLibrary.Exception.ErrorCode;
import com.app.QuizService.DTO.BaseDTO.Question;
import com.app.QuizService.DTO.BaseDTO.QuestionEdit;
import com.app.QuizService.DTO.Request.EditQuizRequest;
import com.app.QuizService.DTO.Request.QuestionDelRequest;
import com.app.QuizService.DTO.Request.QuestionEditRequest;
import com.app.QuizService.DTO.Response.QuizDetail.QuizResponse;
import com.app.QuizService.Entity.Elastic.SearchQuiz;
import com.app.QuizService.Entity.Quiz;
import com.app.QuizService.Mapper.QuestionMapper;
import com.app.QuizService.Mapper.QuizMapper;
import com.app.QuizService.Repository.HttpClient.UserClient;
import com.app.QuizService.Repository.QuizRepository;
import com.app.QuizService.Repository.SearchQuizRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class QuizServiceTest {
    @InjectMocks QuizService quizService;

    @Mock QuizRepository quizRepository;
    @Mock SearchQuizRepository searchQuizRepository;
    @Mock QuizMapper quizMapper;
    @Mock QuestionMapper questionMapper;
    @Mock UserClient userClient;

    @Mock
    QuizResponse quizResponse;
    @Mock
    Quiz quiz;

    Question questionSelect;
    Question questionEnter;
    QuestionEdit questionEditSelect ;
    QuestionEdit questionEditEnter;
    @BeforeEach
    void initData(){
        questionSelect = Question.builder()
                .type("SELECT")
                .options(List.of("O1","O2"))
                .corrects(List.of("O1"))
                .build();

        questionEnter = Question.builder()
                .type("ENTER")
                .content("name =@=, full name =@=")
                .corrects(List.of("tan", "letan"))
                .build();
        questionEditSelect = QuestionEdit.builder().content("questionEditSelect").build();
        questionEditEnter =QuestionEdit.builder().content("questionEditEnter").build();

        lenient().when(quiz.getQuestions()).thenReturn(new HashMap<>(Map.of(0,new Question(),1,new Question())));
        lenient().when(questionMapper.toQuestion(questionEditSelect)).thenReturn(questionSelect);
        lenient().when(questionMapper.toQuestion(questionEditEnter)).thenReturn(questionEnter);
    }

    @Test
    void save_success_create(){
        EditQuizRequest request = mock(EditQuizRequest.class);
        SearchQuiz searchQuiz = mock(SearchQuiz.class);
        Quiz update = quiz;
        Map<Integer, Question> questions = new HashMap<>();
        when(request.getTeacherID()).thenReturn("teacherID");

        when(quizMapper.toQuiz(eq(request))).thenReturn(quiz);
        when(quizRepository.save(any())).thenReturn(quiz);
        when(quizMapper.toSearchQuiz(quiz)).thenReturn(searchQuiz);
        when(quizMapper.toQuizResponse(any())).thenReturn(quizResponse);

        QuizResponse response = quizService.save(request);

        verify(userClient).findById(eq(request.getTeacherID()));
        verify(quizRepository,never()).findById(any());
        verify(quiz,never()).setQuestions(any());
        verify(searchQuizRepository).save(searchQuiz);

        assertThat(quizResponse).isEqualTo(response);
    }
    @Test
    void save_success_update(){
        EditQuizRequest request = mock(EditQuizRequest.class);
        SearchQuiz searchQuiz = mock(SearchQuiz.class);
        Quiz update = quiz;
        Map<Integer, Question> questions = new HashMap<>();
        when(request.getQuizID()).thenReturn("quizID");
        when(request.getTeacherID()).thenReturn("teacherID");
        when(update.getQuestions()).thenReturn(questions);

        when(quizMapper.toQuiz(eq(request))).thenReturn(quiz);
        when(quizRepository.findById(eq(request.getQuizID()))).thenReturn(Optional.of(update));
        when(quizRepository.save(any())).thenReturn(quiz);
        when(quizMapper.toSearchQuiz(quiz)).thenReturn(searchQuiz);
        when(quizMapper.toQuizResponse(any())).thenReturn(quizResponse);

        QuizResponse response = quizService.save(request);

        verify(userClient).findById(eq(request.getTeacherID()));
        verify(quiz).setQuestions(eq(questions));
        verify(searchQuizRepository).save(searchQuiz);

        assertThat(quizResponse).isEqualTo(response);
    }

    @Test
    void save_fail_update_QUIZ_NO_EXISTS(){
        EditQuizRequest request = mock(EditQuizRequest.class);
        SearchQuiz searchQuiz = mock(SearchQuiz.class);
        when(request.getQuizID()).thenReturn("quizID");
        when(request.getTeacherID()).thenReturn("teacherID");

        when(quizMapper.toQuiz(eq(request))).thenReturn(quiz);
        when(quizRepository.findById(eq(request.getQuizID()))).thenReturn(Optional.empty());

        var exception = assertThrows(AppException.class,()->quizService.save(request));

        verify(userClient).findById(eq(request.getTeacherID()));
        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.QUIZ_NO_EXISTS);
    }

    @Test
    void saveQuestion_success(){
        QuestionEditRequest request = mock(QuestionEditRequest.class);

        when(quizRepository.findById(eq(request.getQuizID()))).thenReturn(Optional.of(quiz));
        when(request.getQuestions()).thenReturn(List.of(questionEditSelect,questionEditEnter));
        when(quizRepository.save(any())).thenReturn(quiz);
        when(quizMapper.toQuizResponse(quiz)).thenReturn(quizResponse);

        QuizResponse response = quizService.saveQuestion(request);

        verify(quiz,times(3)).getQuestions();
        verify(questionMapper,times(2)).toQuestion(any(QuestionEdit.class));

        assertThat(response).isEqualTo(quizResponse);
    }

    @Test
    void saveQuestion_fail_CORRECT_INVALID_type_SELECT(){
        QuestionEditRequest request = mock(QuestionEditRequest.class);
        questionSelect.setCorrects(List.of("O3"));

        when(quizRepository.findById(eq(request.getQuizID()))).thenReturn(Optional.of(quiz));
        when(request.getQuestions()).thenReturn(List.of(questionEditSelect,questionEditEnter));

        var exception = assertThrows(AppException.class,
                ()->quizService.saveQuestion(request));

        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.CORRECT_INVALID);
    }

    @Test
    void saveQuestion_fail_CORRECT_INVALID_type_ENTER(){
        QuestionEditRequest request = mock(QuestionEditRequest.class);
        questionEnter.setCorrects(List.of("tan"));

        when(quizRepository.findById(eq(request.getQuizID()))).thenReturn(Optional.of(quiz));
        when(request.getQuestions()).thenReturn(List.of(questionEditSelect,questionEditEnter));

        var exception = assertThrows(AppException.class,
                ()->quizService.saveQuestion(request));

        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.CORRECT_INVALID);
    }

    @Test
    void deleteQuestion_success(){
        QuestionDelRequest request = QuestionDelRequest.builder()
                .quizID("quizID")
                .questions(List.of(0,1))
                .build();
        when(quizRepository.findById(eq(request.getQuizID()))).thenReturn(Optional.of(quiz));
        when(quizRepository.save(any())).thenReturn(quiz);
        when(quizMapper.toQuizResponse(quiz)).thenReturn(quizResponse);

        QuizResponse response = quizService.deleteQuestion(request);

        verify(quiz,times(2)).getQuestions();
        assertThat(response).isEqualTo(quizResponse);
    }

    @Test
    void deleteQuestion_fail_QUESTION_NO_EXISTS(){
        QuestionDelRequest request = QuestionDelRequest.builder()
                .quizID("quizID")
                .questions(List.of(2))
                .build();
        when(quizRepository.findById(eq(request.getQuizID()))).thenReturn(Optional.of(quiz));

        var exception = assertThrows(AppException.class,
                ()->quizService.deleteQuestion(request));

        verify(quiz).getQuestions();
        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.QUESTION_NO_EXISTS);
    }


}
