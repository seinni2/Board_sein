package com.sein.service;

import com.sein.dto.UserDto;
import com.sein.entity.User;
import com.sein.entity.UserRoleEnum;
import com.sein.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public void signUp(UserDto.SignUpRequestDto signUpRequestDto, UserRoleEnum userRoleEnum) {
        String password = signUpRequestDto.getPassword();
        String username = signUpRequestDto.getUsername();

        String encodePwd = passwordEncoder.encode(password);

        User user = new User(encodePwd, username, userRoleEnum);
        userRepository.save(user);
    }

    public UserRoleEnum login(UserDto.LoginRequestDto loginRequestDto) {
       User user = userRepository.findByUsername(loginRequestDto.getUsername()).orElseThrow(()-> new IllegalArgumentException("존재하지 않는 아이디입니다."));

       if(!passwordEncoder.matches(loginRequestDto.getPassword(), user.getPassword())){
           throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
       }
       return user.getRole();
    }
}
