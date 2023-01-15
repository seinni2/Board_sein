package com.sein.controller;

import com.sein.dto.CommentDto;
import com.sein.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comment")
public class CommentController {

    private final CommentService commentService;

    //해당하는 게시글의 댓글 모두 가져오기
    @GetMapping("/{boardId}")
    @Secured({"ROLE_ADMIN","ROLE_USER"})
    public ResponseEntity allComments(@PathVariable Long boardId){
         List<CommentDto.CommentReponseDto> commentAll = commentService.getCommentAll(boardId);
         if(commentAll.isEmpty()){
             return new ResponseEntity("현재 존재하는 댓글이 없습니다.", HttpStatus.OK);
         }
         return new ResponseEntity(commentAll, HttpStatus.OK);
    }

    //댓글 작성
    @PostMapping("/board/{boardId}")
    @Secured({"ROLE_ADMIN","ROLE_USER"})
    public ResponseEntity writeComment(@PathVariable Long boardId, @RequestBody CommentDto.CommentRequestDto commentRequestDto,@AuthenticationPrincipal UserDetails userDetails){
        commentService.wirteComment(boardId, commentRequestDto, userDetails);
        return new ResponseEntity("댓글 작성이 완료되었습니다.", HttpStatus.OK);
    }

    
    //댓글 수정
    @PutMapping("/{commentId}")
    @Secured({"ROLE_ADMIN","ROLE_USER"})
    public ResponseEntity commentModify(@PathVariable Long commentId, @RequestBody CommentDto.CommentRequestDto commentRequestDto, @AuthenticationPrincipal UserDetails userDetails){
        CommentDto.CommentReponseDto modifyComment = commentService.commentModify(commentId, commentRequestDto, userDetails);
        return new ResponseEntity(modifyComment, HttpStatus.OK);
    }

    //댓글 삭제
    @DeleteMapping("/{commentId}")
    @Secured({"ROLE_ADMIN","ROLE_USER"})
    public ResponseEntity commentDelete(@PathVariable Long commentId, @AuthenticationPrincipal UserDetails userDetails){
        commentService.commentDelete(commentId, userDetails);
        return new ResponseEntity("댓글이 성공적으로 삭제되었습니다.", HttpStatus.OK);
    }
}
