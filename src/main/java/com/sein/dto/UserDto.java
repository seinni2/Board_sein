package com.sein.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Pattern;

public class UserDto {

    @Getter
    @NoArgsConstructor
    public class SignUpRequestDto{
        @Pattern(regexp = "^[0-9a-zA-Z]{6,15}$", message = "아이디는 영소문자,숫자로 구성된 6글자 이상 15자 이하로 조합하시오.")
        private String userId;
        @Pattern(regexp = "^[가-힣]{4,10}$", message = "최소 4자 이상, 10자 이하 한글만 입력해주세요.")
        private String username;
        @Pattern(regexp = "^.*(?=^.{8,15}$)(?=.*\\d)(?=.*[a-zA-Z])(?=.*[!@#$%^&+=]).*$", message = "비밀번호는 영대소문자,숫자,특수문자로 구성된 8자 이상 15자 이하로 조합하시오.")
        private String password;
        private boolean admin = false;
        private String adminToken="";

        public SignUpRequestDto(String userId, String username, String password){
            this.userId = userId;
            this.username = username;
            this.password = password;
        }
    }

    @Getter
    @NoArgsConstructor
    public class LoginRequestDto{
        private String userId;
        private String password;

        public LoginRequestDto(String userId, String password){
            this.userId = userId;
            this.password = password;
        }
    }
}
