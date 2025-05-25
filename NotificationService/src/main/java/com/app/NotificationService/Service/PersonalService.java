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
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@Slf4j
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
                .orElseThrow(()-> new AppException(ErrorCode.NOTIFICATION_NO_EXISTS));

        List<NotificationBase> display_list = personal.getNotifications().stream()
                .filter(notificationBase -> Instant.now().isAfter(notificationBase.getDisplayTime()))
                .toList();
        personal.setNotifications(display_list);
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


        Personal response = personalRepository.save(personal);
        taskScheduler.schedule(()->delay_save(personal), request.getDisplayTime());

        return personalMapper.toPersonalResponse(personal);
    }

    void delay_save(Personal personal){
        messagingTemplate.convertAndSendToUser(personal.getName(), "/queue/notification",
                personalMapper.toPersonalResponse(personal));
    }










}
