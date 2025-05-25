package com.app.NotificationService.Mapper;

import com.app.NotificationService.DTO.BaseDTO.NotificationBase;
import com.app.NotificationService.DTO.Request.PersonalSaveRequest;
import com.app.NotificationService.DTO.Response.NotificationResponse;
import com.app.NotificationService.DTO.Response.PersonalResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface NotificationMapper {
    NotificationResponse toNotificationResponse(NotificationBase notificationBase);
    NotificationBase toNotificationBase(PersonalSaveRequest request);
}
