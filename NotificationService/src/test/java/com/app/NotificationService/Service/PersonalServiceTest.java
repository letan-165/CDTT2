package com.app.NotificationService.Service;

import com.app.NotificationService.DTO.BaseDTO.NotificationBase;
import com.app.NotificationService.DTO.Request.PersonalSaveRequest;
import com.app.NotificationService.DTO.Response.PersonalResponse;
import com.app.NotificationService.Entity.Personal;
import com.app.NotificationService.Mapper.NotificationMapper;
import com.app.NotificationService.Mapper.PersonalMapper;
import com.app.NotificationService.Repository.HttpClient.UserClient;
import com.app.NotificationService.Repository.PersonalRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.TaskScheduler;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PersonalServiceTest {
    @InjectMocks
    PersonalService personalService;

    @Mock PersonalRepository personalRepository;
    @Mock PersonalMapper personalMapper;
    @Mock NotificationMapper notificationMapper;
    @Mock UserClient userClient;
    @Mock SimpMessagingTemplate messagingTemplate;
    @Mock TaskScheduler taskScheduler;

    Personal personal;
    PersonalSaveRequest request;
    @Mock PersonalResponse personalResponse;
    @BeforeEach
    void initData(){
        NotificationBase n1 = NotificationBase.builder().index(1).build();
        NotificationBase n2 = NotificationBase.builder().index(2).build();
        personal = Personal.builder()
                .name("tan")
                .notifications(new ArrayList<>(List.of(n1,n2)))
                .build();
        request = PersonalSaveRequest.builder()
                .name("tan")
                .displayTime(Instant.now().plus(5,ChronoUnit.MINUTES))
                .build();
    }

    @Test
    void save_success_create(){
        when(personalRepository.findById(eq(request.getName()))).thenReturn(Optional.empty());

        when(notificationMapper.toNotificationBase(any())).thenReturn(personal.getNotifications().get(0));
        when(personalRepository.save(any())).thenReturn(personal);
        when(personalMapper.toPersonalResponse(any())).thenReturn(personalResponse);

        PersonalResponse response = personalService.save(request);

        verify(taskScheduler).schedule(any(),eq(request.getDisplayTime()));
        assertThat(response).isEqualTo(personalResponse);
    }

    @Test
    void save_success_update(){
        when(personalRepository.findById(eq(request.getName()))).thenReturn(Optional.of(personal));

        when(notificationMapper.toNotificationBase(any())).thenReturn(personal.getNotifications().get(0));
        when(personalRepository.save(any())).thenReturn(personal);
        when(personalMapper.toPersonalResponse(any())).thenReturn(personalResponse);

        PersonalResponse response = personalService.save(request);

        verify(taskScheduler).schedule(any(),eq(request.getDisplayTime()));
        assertThat(response).isEqualTo(personalResponse);
    }

}
