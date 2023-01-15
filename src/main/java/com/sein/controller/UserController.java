package com.sein.controller;

import com.sein.dto.UserDto;
import com.sein.entity.UserRoleEnum;
import com.sein.jwt.JwtUtil;
import com.sein.repository.UserRepository;
import com.sein.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    @Value("${admin.token}")
    private String adminToken;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @PostMapping("/signup")
    public ResponseEntity signUp(@RequestBody @Validated UserDto.SignUpRequestDto signUpRequestDto, BindingResult bindingResult) {

        //사용자 입력한 값 Validated 체크 했을 때 에러 발생할 경우
        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(bindingResult.getAllErrors().toString());
        }

        //있는 사용자 있는 먼저 체크
        if(userRepository.findByUsername(signUpRequestDto.getUsername()).isPresent()){
            return new ResponseEntity("이미 존재하는 사용자입니다.", HttpStatus.BAD_REQUEST);
        }

        UserRoleEnum role = UserRoleEnum.USER;

        //가입하고자 하는 권한이 User인지 Admin인지 확인해주는 절차
        if (signUpRequestDto.isAdmin()) {
            if (!adminToken.equals(signUpRequestDto.getAdminToken())) {
                throw new IllegalArgumentException("잘못된 토큰 값입니다.");
            }
            role = UserRoleEnum.ADMIN;
        }

        userService.signUp(signUpRequestDto, role);
        return ResponseEntity.ok("회원가입이 성공적으로 완료되었습니다.");
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody UserDto.LoginRequestDto loginRequestDto, HttpServletResponse response){
        UserRoleEnum role = userService.login(loginRequestDto);

        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(loginRequestDto.getUsername(), role));
        return ResponseEntity.ok("로그인이 정상적으로 완료되었습니다.");
    }
}
