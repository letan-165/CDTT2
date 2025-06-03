package com.quizz.AccountService.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import quizz.library.common.Exception.GlobalExceptionHandling;

@Configuration
public class CommonConfig {
    @Bean
    public GlobalExceptionHandling globalExceptionHandling(){
        return new GlobalExceptionHandling();
    }
}
