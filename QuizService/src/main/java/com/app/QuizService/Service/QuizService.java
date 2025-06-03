package com.app.QuizService.Service;

import com.app.QuizService.DTO.BaseDTO.Question;
import com.app.QuizService.DTO.Request.EditQuizRequest;
import com.app.QuizService.DTO.Request.QuestionEditRequest;
import com.app.QuizService.DTO.Request.QuestionDelRequest;
import com.app.QuizService.DTO.Response.QuizDetail.QuizResponse;
import com.app.QuizService.Entity.Elastic.SearchQuiz;
import com.app.QuizService.Entity.Quiz;
import com.app.QuizService.Mapper.QuestionMapper;
import com.app.QuizService.Mapper.QuizMapper;
import com.app.QuizService.Repository.HttpClient.UserClient;
import com.app.QuizService.Repository.MongoDB.QuizRepository;
import com.app.QuizService.Repository.ElasticSearch.SearchQuizRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import quizz.library.common.Exception.AppException;
import quizz.library.common.Exception.ErrorCode;

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

    public List<QuizResponse> findAllByTeacherID(String teacherID){
        try{
            userClient.findById(teacherID);
        } catch (Exception e) {
            throw new AppException(ErrorCode.USER_NO_EXIST);
        }
        var list = quizRepository.findAllByTeacherID(teacherID);
        return list.stream()
                .map(quizMapper::toQuizResponse)
                .toList();
    }

    public QuizResponse save(EditQuizRequest request){
        try{
            userClient.findById(request.getTeacherID());
        } catch (Exception e) {
            throw new AppException(ErrorCode.USER_NO_EXIST);
        }
        Quiz quiz = quizMapper.toQuiz(request);
        if(request.getQuizID()!=null){
            Quiz update = quizRepository.findById(request.getQuizID())
                    .orElseThrow(()->new AppException(ErrorCode.QUIZ_NO_EXISTS));
            quiz.setQuestions(update.getQuestions());
        }
        Quiz response = quizRepository.save(quiz);
        searchQuizRepository.save(quizMapper.toSearchQuiz(response));
        return quizMapper.toQuizResponse(response);
    }

    public QuizResponse saveQuestion(QuestionEditRequest request){
        Quiz quiz = quizRepository.findById(request.getQuizID())
                .orElseThrow(()->new AppException(ErrorCode.QUIZ_NO_EXISTS));

        var map = quiz.getQuestions();
        int index = map.isEmpty() ? -1 : Collections.max(map.keySet());
        for(var questionEdit : request.getQuestions()){
            int key;
            Question question = questionMapper.toQuestion(questionEdit);
            if(question.getType().equals("SELECT") && !question.checkSelect(question.getCorrects())){
                throw new AppException(ErrorCode.CORRECT_INVALID);
            }

            if(question.getType().equals("ENTER")){
                question.setOptions(Collections.emptyList());
                if(!question.checkEnter(question.getCorrects()))
                    throw new AppException(ErrorCode.CORRECT_INVALID);
            }

            if(questionEdit.getQuestionID() == null){
                index +=1 ;
                key = index;
            }else{
                key = questionEdit.getQuestionID();
            }
            question.setQuestionID(key);
            quiz.getQuestions().put(key,question);
        }
        return quizMapper.toQuizResponse(quizRepository.save(quiz));
    }

    public QuizResponse deleteQuestion(QuestionDelRequest request){
        Quiz quiz = quizRepository.findById(request.getQuizID())
                .orElseThrow(()->new AppException(ErrorCode.QUIZ_NO_EXISTS));

        request.getQuestions().forEach(questionID->{
            var check = quiz.getQuestions().remove(questionID);
            if(check==null)
                throw new AppException(ErrorCode.QUESTION_NO_EXISTS);
        });

        return quizMapper.toQuizResponse(quizRepository.save(quiz));
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
