package com.quizz.AccountService.Config;

import com.quizz.AccountService.Entity.Permission;
import com.quizz.AccountService.Entity.Role;
import com.quizz.AccountService.Enum.TypeRole;
import com.quizz.AccountService.Repository.PermissionRepository;
import com.quizz.AccountService.Repository.RoleRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class AppRunner {
    RoleRepository roleRepository;
    PermissionRepository permissionRepository;

    @Bean
    ApplicationRunner applicationRunner(){
        return ApplicationRunner -> {
            List<Role> roles = new ArrayList<>(List.of(
                    new Role(TypeRole.ADMIN.name(), "Quản lí hệ thống", Collections.emptyList()),
                    new Role(TypeRole.TEACHER.name(), "Giáo viên đưa ra các chủ đề học tập", Collections.emptyList()),
                    new Role(TypeRole.STUDENT.name(), "Học sinh ôn tập các chủ đề", Collections.emptyList())
            ));
            if (!roleRepository.existsById(roles.get(0).getName())) {
                roleRepository.saveAll(roles);
            }
        };
    }
}
