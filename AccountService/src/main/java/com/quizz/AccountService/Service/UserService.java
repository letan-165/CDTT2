package com.quizz.AccountService.Service;

import com.quizz.AccountService.DTO.Request.ForgotPassRequest;
import com.quizz.AccountService.DTO.Request.UserSignUpRequest;
import com.quizz.AccountService.DTO.Response.UserResponse;
import com.quizz.AccountService.Entity.Role;
import com.quizz.AccountService.Entity.User;
import com.quizz.AccountService.Enum.TypeRole;
import com.quizz.AccountService.Exception.AppException;
import com.quizz.AccountService.Exception.ErrorCode;
import com.quizz.AccountService.Mapper.UserMapper;
import com.quizz.AccountService.Repository.OtpRepository;
import com.quizz.AccountService.Repository.RoleRepository;
import com.quizz.AccountService.Repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    public List<User> findAll(){
        return userRepository.findAll();
    }

    public UserResponse signUp(UserSignUpRequest request) {
        otpService.verify(request.getEmail(), request.getOtp());

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





}
