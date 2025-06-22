package com.app.QuizService.Controller;

import com.app.QuizService.DTO.Request.QuestionEditRequest;
import com.app.QuizService.DTO.Request.EditQuizRequest;
import com.app.QuizService.DTO.Request.QuestionDelRequest;
import com.app.QuizService.DTO.Request.SearchRequest;
import com.app.QuizService.DTO.Response.QuizDetail.QuizResponse;
import com.app.QuizService.Entity.Elastic.SearchQuiz;
import com.app.QuizService.Entity.Quiz;
import com.app.QuizService.Service.QuizService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import quizz.library.common.DTO.ApiResponse;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/quiz")
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class QuizController {
    QuizService quizService;

    @GetMapping("/public")
    ApiResponse<List<QuizResponse>>findAllPublic(){
        return ApiResponse.<List<QuizResponse>>builder()
                .message("Lấy danh sách bài quiz công khai: ")
                .result(quizService.findAllPublic())
                .build();
    }

    @GetMapping("/public/teacher/{teacherID}")
    ApiResponse<List<QuizResponse>>findAllByTeacher(@PathVariable String teacherID){
        return ApiResponse.<List<QuizResponse>>builder()
                .message("Lấy danh sách bài quiz của giáo viên: "+teacherID)
                .result(quizService.findAllByTeacherID(teacherID))
                .build();
    }

    @PostMapping("/public")
    ApiResponse<QuizResponse> save(@Valid @RequestBody EditQuizRequest request){
        return ApiResponse.<QuizResponse>builder()
                .message("Khởi tạo quiz của: "+request.getTeacherID())
                .result(quizService.save(request))
                .build();
    }

    @GetMapping("/public/{quizID}")
    ApiResponse<QuizResponse> findById(@PathVariable String quizID){
        return ApiResponse.<QuizResponse>builder()
                .message("Tìm quiz: "+quizID)
                .result(quizService.findById(quizID))
                .build();
    }


    @PutMapping("/public/question")
    ApiResponse<QuizResponse>saveQuestion(@RequestBody QuestionEditRequest request){
        return ApiResponse.<QuizResponse>builder()
                .message("Thêm câu hỏi vào quiz: "+request.getQuizID())
                .result(quizService.saveQuestion(request))
                .build();
    }

    @PutMapping("/public/question/delete")
    ApiResponse<QuizResponse>deleteQuestion(@RequestBody QuestionDelRequest request){
        return ApiResponse.<QuizResponse>builder()
                .message("Xóa câu hỏi của quiz: "+request.getQuizID())
                .result(quizService.deleteQuestion(request))
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
    ApiResponse<Set<String>>statisticsTopic(@RequestBody SearchRequest request){
        return ApiResponse.<Set<String>>builder()
                .message("Thống kê chủ đề "+ request.getSearch())
                .result(quizService.searchTagTopic(request.getSearch()))
                .build();
    }



}
