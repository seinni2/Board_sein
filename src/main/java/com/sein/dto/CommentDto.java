package com.sein.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

public class CommentDto {

    @Getter
    @NoArgsConstructor
    public static class CommentRequestDto{
        private String comment;

        public CommentRequestDto(String comment){
            this.comment = comment;
        }
    }

    @Getter
    @NoArgsConstructor
    public static class CommentReponseDto{
        private String username;

        private String comment;

        private Long likeCount;

        public CommentReponseDto(String username, String comment, Long likeCount){
            this.username = username;
            this.comment = comment;
            this.likeCount = likeCount;
        }
    }
}
