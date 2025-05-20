package com.app.QuizService.Validation;

import com.app.QuizService.DTO.Request.SaveQuizRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.Duration;

public class QuizTimeValidator implements ConstraintValidator<ValidQuizTime, SaveQuizRequest> {

    @Override
    public boolean isValid(SaveQuizRequest request, ConstraintValidatorContext context) {
        if (request.getStartTime() == null || request.getEndTime() == null || request.getDuration() == null)
            return fail(context,"FIELD_TIME_NOTNULL");

        if (request.getStartTime().isAfter(request.getEndTime()))
            return fail(context,"BETWEEN_TIME_INVALID");

        Duration expectedDuration = Duration.between(request.getStartTime(), request.getEndTime());
        if(expectedDuration.compareTo(request.getDuration()) < 0)
            return fail(context,"DURATION_FIELD_INVALID");

        return true;
    }

    private boolean fail(ConstraintValidatorContext context, String message) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
        return false;
    }
}

