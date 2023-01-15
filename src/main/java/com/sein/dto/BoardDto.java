package com.sein.dto;

import com.sein.entity.Comment;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

public class BoardDto {

    @Getter
    @NoArgsConstructor
    public static class BoardRequestDto{

        private String title;

        private String content;

        public BoardRequestDto(String title, String content){
            this.title = title;
            this.content = content;
        }
    }

    @Getter
    @NoArgsConstructor
    public static class BoardResponseDto{

        private String username;

        private String title;

        private String content;

        private long likeCount;


        public BoardResponseDto(String username, String title, String content, Long likeCount){
            this.username = username;
            this.title = title;
            this.content = content;
            this.likeCount = likeCount;
        }
    }
}
