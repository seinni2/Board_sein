package com.sein.controller;

import com.sein.dto.BoardDto;
import com.sein.security.UserDetailImpl;
import com.sein.service.BoardSerivce;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class BoardController {

    private final BoardSerivce boardSerivce;

    //전체 글 조회
    @GetMapping("/board")
    public ResponseEntity getBoardAllList(){
        List<BoardDto.BoardResponseDto> boardAllList = boardSerivce.getBoardAllList();
        if(boardAllList.isEmpty()){
            return new ResponseEntity("현재 작성된 게시물이 존재하지 않습니다.", HttpStatus.OK);
        }

        return new ResponseEntity(boardAllList, HttpStatus.OK);
    }

    //글 작성
    @PostMapping("/board")
    @Secured({"ROLE_ADMIN","ROLE_USER"})
    public ResponseEntity boardWrite(@RequestBody BoardDto.BoardRequestDto boardRequestDto, @AuthenticationPrincipal UserDetailImpl userDetail){
        boardSerivce.boardWriter(boardRequestDto, userDetail);
        return new ResponseEntity("게시물이 성공적으로 작성되었습니다", HttpStatus.OK);
    }

    //글 조회
    @GetMapping("/board/{boardId}")
    @Secured({"ROLE_ADMIN","ROLE_USER"})
    public ResponseEntity getBoard(@PathVariable Long boardId){
        BoardDto.BoardResponseDto board = boardSerivce.getBoard(boardId);
        return new ResponseEntity(board, HttpStatus.OK);
    }
    //글 수정
    @PutMapping("/board/{boardId}")
    @Secured({"ROLE_ADMIN","ROLE_USER"})
    public ResponseEntity updateBoard(@PathVariable Long boardId, @RequestBody BoardDto.BoardRequestDto boardRequestDto, @AuthenticationPrincipal UserDetailImpl userDetail){
        BoardDto.BoardResponseDto board = boardSerivce.boardUpdate(boardId,boardRequestDto, userDetail);
        return new ResponseEntity(board, HttpStatus.OK);
    }
    //글 삭제
    @DeleteMapping("/board/{boardId}")
    @Secured({"ROLE_ADMIN","ROLE_USER"})
    public ResponseEntity deleteBoard(@PathVariable Long boardId,@AuthenticationPrincipal UserDetailImpl userDetail){
        boardSerivce.boardDelete(boardId, userDetail);
        return new ResponseEntity("게시물이 성공적으로 삭제되었습니다.", HttpStatus.OK);
    }
}
