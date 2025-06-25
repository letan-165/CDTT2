package com.app.NotificationService.Mapper;

import com.app.NotificationService.DTO.BaseDTO.NotificationBase;
import com.app.NotificationService.DTO.Request.PersonalSaveRequest;
import com.app.NotificationService.DTO.Response.NotificationResponse;
import com.app.NotificationService.DTO.Response.PersonalResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Mapper(componentModel = "spring")
public interface NotificationMapper {
    @Mapping(target = "displayTime", source = "displayTime", qualifiedByName = "toVNTime")
    NotificationResponse toNotificationResponse(NotificationBase notificationBase);
    NotificationBase toNotificationBase(PersonalSaveRequest request);

    @Named("toVNTime")
    default String toVNTime(Instant time){
        if (time == null) return null;
        ZoneId zoneVN = ZoneId.of("Asia/Ho_Chi_Minh");
        LocalDateTime vnTime = LocalDateTime.ofInstant(time, zoneVN);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

        return vnTime.format(formatter);
    }
}
