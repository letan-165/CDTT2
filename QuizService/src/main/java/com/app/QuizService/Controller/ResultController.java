package com.app.QuizService.Controller;

import com.app.QuizService.DTO.ApiResponse;
import com.app.QuizService.DTO.Request.JoinQuizRequest;
import com.app.QuizService.DTO.Request.SaveQuizRequest;
import com.app.QuizService.DTO.Request.SubmitQuizRequest;
import com.app.QuizService.DTO.Response.Statistics.StatisticsResponse;
import com.app.QuizService.DTO.Response.TodoQuiz.ResultResponse;
import com.app.QuizService.DTO.Response.TodoQuiz.SubmitQuizResponse;
import com.app.QuizService.Service.ResultService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/result")
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class ResultController {
    ResultService resultService;

    @PostMapping("/public/join")
    ApiResponse<SubmitQuizResponse>join(@RequestBody JoinQuizRequest request){
        return ApiResponse.<SubmitQuizResponse>builder()
                .message(request.getStudentID()+" đã tham gia quiz "+request.getQuizID())
                .result(resultService.join(request))
                .build();
    }

    @PutMapping("/public/submit")
    ApiResponse<SubmitQuizResponse>submit(@RequestBody SubmitQuizRequest request){
        return ApiResponse.<SubmitQuizResponse>builder()
                .message("Xác nhận câu trả lời của " + request.getResultID())
                .result(resultService.submit(request))
                .build();
    }

    @PutMapping("/public/{resultID}/finish")
    ApiResponse<ResultResponse> finish(@PathVariable String resultID){
        return ApiResponse.<ResultResponse>builder()
                .message("Xác nhận kết thúc quiz của: "+resultID)
                .result(resultService.finish(resultID))
                .build();
    }

    @GetMapping("/public/{resultID}")
    ApiResponse<ResultResponse>findByID(@PathVariable String resultID){
        return ApiResponse.<ResultResponse>builder()
                .message("Lấy kết quả "+ resultID)
                .result(resultService.findByID(resultID))
                .build();
    }

    @GetMapping("/public/quiz/{quizID}/statistics")
    ApiResponse<StatisticsResponse>statistics(@PathVariable String quizID){
        return ApiResponse.<StatisticsResponse>builder()
                .message("Thống kê kết quả của quiz: "+quizID)
                .result(resultService.statistic(quizID))
                .build();
    }

    @GetMapping("/public/student/{studentID}/statistics")
    ApiResponse<List<ResultResponse>>statisticStudents(@PathVariable String studentID){
        return ApiResponse.<List<ResultResponse>>builder()
                .message("Thống kê kết quả của học sinh:"+studentID)
                .result(resultService.statisticStudents(studentID))
                .build();
    }

}
