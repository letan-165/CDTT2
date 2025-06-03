package com.app.NotificationService.Mapper;

import com.app.NotificationService.DTO.BaseDTO.NotificationBase;
import com.app.NotificationService.DTO.Response.NotificationResponse;
import com.app.NotificationService.DTO.Response.PersonalResponse;
import com.app.NotificationService.Entity.Personal;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class PersonalMapper {
    @Autowired
    NotificationMapper notificationMapper;

    @Mapping(target = "notifications", source = "notifications", qualifiedByName = "toNotificationResponses")
    public abstract PersonalResponse toPersonalResponse(Personal personal);

    @Named("toNotificationResponses")
    List<NotificationResponse> toNotificationResponse(List<NotificationBase> notifications){
        return notifications.stream().map(notificationMapper::toNotificationResponse).toList();
    }
}
