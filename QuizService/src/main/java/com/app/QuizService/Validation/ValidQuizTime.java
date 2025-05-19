package com.app.QuizService.Validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = QuizTimeValidator.class)
public @interface ValidQuizTime {
    String message() default "Lỗi thời gian khi tạo quiz";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
