package com.app.QuizService.Service;

import com.app.QuizService.DTO.BaseDTO.Question;
import com.app.QuizService.DTO.Request.Client.SendNotificationRequest;
import com.app.QuizService.DTO.Request.JoinQuizRequest;
import com.app.QuizService.DTO.Request.SubmitQuizRequest;
import com.app.QuizService.DTO.Response.Client.UserResponse;
import com.app.QuizService.DTO.Response.Statistics.StatisticsResponse;
import com.app.QuizService.DTO.Response.TodoQuiz.ResultResponse;
import com.app.QuizService.DTO.Response.TodoQuiz.SubmitQuizResponse;
import com.app.QuizService.Entity.Quiz;
import com.app.QuizService.Entity.Result;
import com.app.QuizService.Exception.AppException;
import com.app.QuizService.Exception.ErrorCode;
import com.app.QuizService.Mapper.QuestionMapper;
import com.app.QuizService.Mapper.ResultMapper;
import com.app.QuizService.Repository.HttpClient.NotificationClient;
import com.app.QuizService.Repository.HttpClient.UserClient;
import com.app.QuizService.Repository.QuizRepository;
import com.app.QuizService.Repository.ResultRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

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
    NotificationClient notificationClient;

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
        UserResponse userResponse = null;
        try{
            userResponse = userClient.findById(request.getStudentID()).getResult();
        } catch (Exception e) {
            throw new AppException(ErrorCode.USER_NO_EXIST);
        }
        Quiz quiz = quizRepository.findById(request.getQuizID())
                .orElseThrow(()-> new AppException(ErrorCode.QUIZ_NO_EXISTS));

        Result result = Result.builder()
                .quiz(quiz)
                .studentID(request.getStudentID())
                .score(0)
                .startTime(Instant.now())
                .build();

        Result response = resultRepository.save(result);
        if(Instant.now().plus(5,ChronoUnit.MINUTES).isBefore(quiz.getStartTime())){
            notificationClient.sendNotification(SendNotificationRequest.builder()
                            .name(userResponse.getName())
                            .subject("Thông báo có bài quiz sắp diễn ra")
                            .content("Bài quiz "+ quiz.getTitle() + "còn 5 phút sẽ bắt đầu làm bài vui lòng chuẩn bị.")
                            .displayTime(quiz.getStartTime().minus(5,ChronoUnit.MINUTES))
                    .build());
        }
        notificationClient.sendNotification(SendNotificationRequest.builder()
                .name(userResponse.getName())
                .subject("Thông báo bắt đầu làm bài")
                .content("Bài quiz "+ quiz.getTitle() + " đã mở vui lòng tham gia")
                .displayTime(quiz.getStartTime())
                .build());


        return toSubmitQuizResponse(response);
    }

    public SubmitQuizResponse submit(SubmitQuizRequest request){
        Result result = resultRepository.findById(request.getResultID())
                .orElseThrow(()->new AppException(ErrorCode.RESULT_NO_EXISTS));

        if(Instant.now().isBefore(result.getQuiz().getStartTime()))
            throw new AppException(ErrorCode.TIME_TODO_INVALID);


        request.getQuestions().forEach(questionRequest ->{
            Quiz quiz = result.getQuiz();
            //Give question from ID request
            Question question = quiz.getQuestions().get(questionRequest.getQuestionID());
            var answers = questionRequest.getAnswers();
            //Update answer
            question.setAnswers(answers);
            result.getQuiz().getQuestions()
                    .put(questionRequest.getQuestionID(),question);
        });

        return toSubmitQuizResponse(resultRepository.save(result));
    }

    public ResultResponse finish(String resultID){
        Result result = resultRepository.findById(resultID)
                .orElseThrow(()->new AppException(ErrorCode.RESULT_NO_EXISTS));

        int totalCorrectAnswers = (int) result.getQuiz().getQuestions().values().stream()
                .filter(Question::checkAnswer)
                .count();
        double score = (double) 10 /result.getQuiz().getQuestions().size() * totalCorrectAnswers;

        Result response = resultRepository.save(result.toBuilder()
                        .endTime(Instant.now())
                        .totalCorrectAnswers(totalCorrectAnswers)
                        .score(score)
                .build());

        return resultMapper.toResultResponse(response);
    }

    public ResultResponse findByID(String resultID){
        Result result = resultRepository.findById(resultID)
                .orElseThrow(()->new AppException(ErrorCode.RESULT_NO_EXISTS));
        return resultMapper.toResultResponse(resultRepository.save(result));
    }

    public StatisticsResponse statistic(String quizID){
        Quiz quiz = quizRepository.findById(quizID)
                .orElseThrow(()->new AppException(ErrorCode.QUIZ_NO_EXISTS));

        var results = resultRepository.findAll();

        var response = results.stream()
                .filter(result->result.getQuiz().getQuizID().equals(quizID))
                .map(resultMapper::toStatisticsResultResponse)
                .toList();

        return StatisticsResponse.builder()
                .quizID(quizID)
                .results(response)
                .build();
    }

    public List<ResultResponse> statisticStudents(String studentID){
        var resultsByStudent = resultRepository.findAllByStudentID(studentID);
        return resultsByStudent.stream()
                .map(resultMapper::toResultResponse)
                .toList();
    }

    public List<Integer> statisticStudentResultTimes(String studentID,int lastWeek){
        List<Integer> totalTimeLearnOnWeek = new ArrayList<>();
        var resultsByStudent = resultRepository.findAllByStudentID(studentID);
        ZonedDateTime monday = Instant.now()
                .atZone(ZoneId.systemDefault())
                .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
                .minusWeeks(lastWeek)
                .with(LocalTime.MIDNIGHT);

        var resultOnWeeks = resultsByStudent.stream()
                .filter(result -> result.getStartTime().isAfter(monday.toInstant())
                        && result.getEndTime().isBefore(monday.plusDays(7).toInstant()))
                .toList();


        for(int i = 0; i < 7; i++){
            Instant day = monday.plusDays(i).toInstant();

            totalTimeLearnOnWeek.add(resultOnWeeks.stream()
                    .filter(result -> checkDayTime(result.getStartTime(),day))
                    .mapToInt(result-> Math.toIntExact(Duration.between(result.getStartTime(), result.getEndTime()).toMinutes()))
                    .sum());
        }

        return totalTimeLearnOnWeek;
    }

    boolean checkDayTime(Instant instant1,Instant instant2){
        ZoneId zone = ZoneId.systemDefault();
        LocalDate date1 = instant1.atZone(zone).toLocalDate();
        LocalDate date2 = instant2.atZone(zone).toLocalDate();
        return date1.getDayOfMonth() == date2.getDayOfMonth();
    }





}
