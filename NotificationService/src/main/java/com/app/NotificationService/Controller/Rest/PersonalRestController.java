package com.app.NotificationService.Controller.Rest;

import com.app.CommonLibrary.DTO.ApiResponse;
import com.app.NotificationService.DTO.Request.PersonalSaveRequest;
import com.app.NotificationService.DTO.Response.PersonalResponse;
import com.app.NotificationService.Service.PersonalService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/personal")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class PersonalRestController {
    PersonalService personalService;
    @PostMapping("/send")
    ApiResponse<PersonalResponse> save(@RequestBody PersonalSaveRequest request){
        return ApiResponse.<PersonalResponse>builder()
                .message("Gửi thông báo đến người dùng " + request.getName() +" Thành công")
                .result(personalService.save(request))
                .build();
    }
}
