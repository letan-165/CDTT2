package com.app.QuizService.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import quizz.library.common.Exception.GlobalExceptionHandling;

@Configuration
public class CommonConfig {
    @Bean
    GlobalExceptionHandling globalExceptionHandling(){
        return new GlobalExceptionHandling();
    }

}
