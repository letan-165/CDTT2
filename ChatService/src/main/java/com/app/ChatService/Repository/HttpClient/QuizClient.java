package com.app.ChatService.Repository.HttpClient;

import com.app.ChatService.DTO.Request.Client.QuestionEditRequest;
import com.app.ChatService.DTO.Response.Client.QuizDetail.QuizResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import quizz.library.common.DTO.ApiResponse;

@FeignClient(name = "quiz", url = "${app.service.quiz}")
public interface QuizClient {
    @GetMapping(value = "/quiz/{quizID}", produces = MediaType.APPLICATION_JSON_VALUE)
    ApiResponse<QuizResponse> findById(@PathVariable String quizID);

    @PutMapping("/quiz/public/question")
    ApiResponse<?> saveQuestion(@RequestBody QuestionEditRequest request);
}
