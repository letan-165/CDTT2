package com.quizz.AccountService.Service;


import com.quizz.AccountService.DTO.Request.ForgotPassRequest;
import com.quizz.AccountService.DTO.Request.SaveLockUserRequest;
import com.quizz.AccountService.DTO.Request.UserSignUpRequest;
import com.quizz.AccountService.DTO.Response.UserResponse;
import com.quizz.AccountService.Entity.MySql.Role;
import com.quizz.AccountService.Entity.MySql.User;
import com.quizz.AccountService.Entity.Redis.LockUser;
import com.quizz.AccountService.Mapper.UserMapper;
import com.quizz.AccountService.Repository.MySql.RoleRepository;
import com.quizz.AccountService.Repository.MySql.UserRepository;
import com.quizz.AccountService.Repository.Redis.LockUserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import quizz.library.common.Exception.AppException;
import quizz.library.common.Exception.ErrorCode;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@Slf4j
public class UserService {
    UserRepository userRepository;
    RoleRepository roleRepository;
    OtpService otpService;
    PasswordEncoder passwordEncoder;
    UserMapper userMapper;
    LockUserRepository lockUserRepository;

    public List<UserResponse> findAll(){
        return userRepository.findAll().stream()
                .map(userMapper::toUserResponse)
                .toList();
    }

    public UserResponse signUp(UserSignUpRequest request) {
        if(userRepository.existsByName(request.getName()))
            throw new AppException(ErrorCode.USER_EXIST);

        Role role = roleRepository.findById(request.getRole())
                .orElseThrow(()-> new AppException(ErrorCode.ROLE_INVALID));

        User user = userMapper.toUser(request);
        user.setRole(role);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        return userMapper.toUserResponse(
                userRepository.save(user));
    }

    public UserResponse findById(String userID) {
        User user = userRepository.findById(userID)
                .orElseThrow(()->new AppException(ErrorCode.USER_NO_EXIST));
        return userMapper.toUserResponse(user);
    }

    public UserResponse findByName(String name) {
        User user = userRepository.findByName(name)
                .orElseThrow(()->new AppException(ErrorCode.USER_NO_EXIST));
        return userMapper.toUserResponse(user);
    }

    public UserResponse forgotPassword(ForgotPassRequest request) {
        User user = userRepository.findByName(request.getUsername())
                .orElseThrow(()->new AppException(ErrorCode.USER_NO_EXIST));
        if(!user.getEmail().equals(request.getEmail()))
            throw new AppException(ErrorCode.EMAIL_INVALID);

        otpService.verify(user.getEmail(), request.getOtp());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        return userMapper.toUserResponse(userRepository.save(user));
    }


    //UserLock

    public List<LockUser> findAllLock() {
        return (List<LockUser>) lockUserRepository.findAll();
    }

    public LockUser saveLockUser(SaveLockUserRequest request) {
        User user = userRepository.findById(request.getUserID())
                .orElseThrow(()->new AppException(ErrorCode.USER_NO_EXIST));

        LockUser lockUser = userMapper.toLockUser(user);
        lockUser.setExpiryTime(request.getExpiryTime());
        lockUserRepository.save(lockUser);

        return lockUserRepository.save(lockUser);
    }

    public Boolean deleteLockUser(String userID) {
        if(!userRepository.existsById(userID))
            throw new AppException(ErrorCode.USER_NO_EXIST);

        if(!lockUserRepository.existsById(userID))
            throw new AppException(ErrorCode.USER_NO_LOCK);

        lockUserRepository.deleteById(userID);

        return true;
    }



}
