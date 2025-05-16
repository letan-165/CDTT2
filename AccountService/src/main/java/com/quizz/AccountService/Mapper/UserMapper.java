package com.quizz.AccountService.Mapper;

import com.quizz.AccountService.DTO.Request.UserSignUpRequest;
import com.quizz.AccountService.DTO.Response.UserResponse;
import com.quizz.AccountService.Entity.Role;
import com.quizz.AccountService.Entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserSignUpRequest request);

    @Mapping(target = "roles",source = "roles", qualifiedByName = "toRoleName")
    UserResponse toUserResponse(User user);

    @Named("toRoleName")
     default List<String> toRoleName(List<Role> roles) {
        return roles.stream().map(Role::getName).toList();
    }
}
