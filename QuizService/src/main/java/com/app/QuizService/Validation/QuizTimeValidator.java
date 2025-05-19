package com.app.QuizService.Validation;

import com.app.QuizService.DTO.Request.SaveQuizRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.Duration;

public class QuizTimeValidator implements ConstraintValidator<ValidQuizTime, SaveQuizRequest> {

    @Override
    public boolean isValid(SaveQuizRequest request, ConstraintValidatorContext context) {
        if (request.getStartTime() == null || request.getEndTime() == null || request.getDuration() == null)
            return fail(context,"Các giá trị thời gian chưa nhập đầy đủ");

        if (request.getStartTime().isAfter(request.getEndTime()))
            return fail(context,"Thời gian bắt đầu nằm sau thời gian kết thúc");

        Duration expectedDuration = Duration.between(request.getStartTime(), request.getEndTime());
        if(expectedDuration.compareTo(request.getDuration()) < 0)
            return fail(context,"Khoảng cách giữa 2 thời gian nhỏ hơn tổng thời gian làm bài");

        return true;
    }

    private boolean fail(ConstraintValidatorContext context, String message) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
        return false;
    }
}

