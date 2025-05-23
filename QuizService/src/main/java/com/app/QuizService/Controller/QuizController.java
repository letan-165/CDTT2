package com.app.QuizService.Controller;

import com.app.QuizService.DTO.ApiResponse;
import com.app.QuizService.DTO.Request.SaveQuizRequest;
import com.app.QuizService.DTO.Request.SearchRequest;
import com.app.QuizService.DTO.Response.QuizDetail.QuizResponse;
import com.app.QuizService.Entity.Elastic.SearchQuiz;
import com.app.QuizService.Service.QuizService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/quiz")
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class QuizController {
    QuizService quizService;

    @PostMapping("/public")
    ApiResponse<QuizResponse>save(@Valid @RequestBody SaveQuizRequest request){
        return ApiResponse.<QuizResponse>builder()
                .message("Khởi tạo quiz của: "+request.getTeacherID())
                .result(quizService.save(request))
                .build();
    }

    @PostMapping("/public/title/search")
    ApiResponse<List<SearchQuiz>>statisticsTitle(@RequestBody SearchRequest request){
        return ApiResponse.<List<SearchQuiz>>builder()
                .message("Thống kê tiêu đề "+ request.getSearch())
                .result(quizService.searchTagTitle(request.getSearch()))
                .build();
    }

    @PostMapping("/public/topic/search")
    ApiResponse<List<String>>statisticsTopic(@RequestBody SearchRequest request){
        return ApiResponse.<List<String>>builder()
                .message("Thống kê chủ đề "+ request.getSearch())
                .result(quizService.searchTagTopic(request.getSearch()))
                .build();
    }



}
