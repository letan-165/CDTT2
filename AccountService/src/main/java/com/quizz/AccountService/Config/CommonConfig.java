package com.quizz.AccountService.Config;

import com.app.CommonLibrary.Exception.GlobalExceptionHandling;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommonConfig {

    @Bean
    public GlobalExceptionHandling globalExceptionHandling(){
        return new GlobalExceptionHandling();
    }
}
