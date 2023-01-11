package com.sein.service;

import com.sein.dto.UserDto;
import com.sein.entity.User;
import com.sein.entity.UserRoleEnum;
import com.sein.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public void signUp(UserDto.SignUpRequestDto signUpRequestDto, UserRoleEnum userRoleEnum) {
        String userId = signUpRequestDto.getUserId();
        String password = signUpRequestDto.getPassword();
        String username = signUpRequestDto.getUsername();

        User user = new User(userId, password, username, userRoleEnum);
        userRepository.save(user);
    }

    public void login(UserDto.LoginRequestDto loginRequestDto) {
       User user = userRepository.findUserByUserId(loginRequestDto.getUserId()).orElseThrow(()-> new IllegalArgumentException("존재하지 않는 아이디입니다."));

       if(!user.getPassword().equals(loginRequestDto.getPassword())){
           throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
       }
    }
}
