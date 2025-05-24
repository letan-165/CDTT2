package com.quizz.AccountService.Mapper;

import com.quizz.AccountService.DTO.Request.UserSignUpRequest;
import com.quizz.AccountService.DTO.Response.UserResponse;
import com.quizz.AccountService.Entity.MySql.Role;
import com.quizz.AccountService.Entity.MySql.User;
import com.quizz.AccountService.Entity.Redis.LockUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "role", ignore = true)
    User toUser(UserSignUpRequest request);

    @Mapping(target = "role",source = "role", qualifiedByName = "toRoleName")
    UserResponse toUserResponse(User user);

    LockUser toLockUser(User user);

    @Named("toRoleName")
     default String toRoleName(Role role) {
        return role.getName();
    }
}
