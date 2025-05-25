package com.app.NotificationService.Service;

import com.app.NotificationService.DTO.BaseDTO.NotificationBase;
import com.app.NotificationService.DTO.Request.PersonalSaveRequest;
import com.app.NotificationService.DTO.Response.PersonalResponse;
import com.app.NotificationService.Entity.Personal;
import com.app.NotificationService.Exception.AppException;
import com.app.NotificationService.Exception.ErrorCode;
import com.app.NotificationService.Mapper.NotificationMapper;
import com.app.NotificationService.Mapper.PersonalMapper;
import com.app.NotificationService.Repository.HttpClient.UserClient;
import com.app.NotificationService.Repository.PersonalRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class PersonalService {
    PersonalRepository personalRepository;
    PersonalMapper personalMapper;
    NotificationMapper notificationMapper;
    UserClient userClient;
    SimpMessagingTemplate messagingTemplate;
    TaskScheduler taskScheduler;

    public PersonalResponse getNotificationPersonal(String name){
        try{
            userClient.findByName(name);
        } catch (Exception e) {
            throw new AppException(ErrorCode.USER_NO_EXIST);
        }
        Personal personal = personalRepository.findById(name)
                .orElseThrow(()-> new RuntimeException("Thông báo chưa khởi tạo"));

        return personalMapper.toPersonalResponse(personal);
    }

    public PersonalResponse save(PersonalSaveRequest request){
        try{
            userClient.findByName(request.getName());
        } catch (Exception e) {
            throw new AppException(ErrorCode.USER_NO_EXIST);
        }
        Personal personal = personalRepository.findById(request.getName())
                .orElse(Personal.builder()
                        .name(request.getName())
                        .notifications(new ArrayList<>())
                        .build());
        int index = personal.getNotifications().stream()
                .mapToInt(NotificationBase::getIndex)
                .max()
                .orElse(-1);

        if(request.getDisplayTime()==null)
            request.setDisplayTime(Instant.now().plus(5,ChronoUnit.SECONDS));

        NotificationBase notificationBase = notificationMapper.toNotificationBase(request);

        personal.getNotifications().add(notificationBase.toBuilder()
                .index(index+1)
                .startTime(Instant.now())
                .endTime(Instant.now().plus(30, ChronoUnit.DAYS))
                .isRead(false)
                .build());

        taskScheduler.schedule(()->delay_save(personal), request.getDisplayTime());

        return personalMapper.toPersonalResponse(personal);
    }

    void delay_save(Personal personal){
        Personal response = personalRepository.save(personal);
        messagingTemplate.convertAndSendToUser(personal.getName(), "/queue/notification",
                personalMapper.toPersonalResponse(response));
    }










}
