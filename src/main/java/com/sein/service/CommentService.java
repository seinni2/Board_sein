package com.sein.service;

import com.sein.dto.CommentDto;
import com.sein.entity.Board;
import com.sein.entity.Comment;
import com.sein.repository.BoardRepository;
import com.sein.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    private final BoardRepository boardRepository;

    @Transactional(readOnly = true)
    public List<CommentDto.CommentReponseDto> getCommentAll(Long boardId) {

       List<Comment> commentList = commentRepository.findByBoardId(boardId);
       List<CommentDto.CommentReponseDto> commentDtoList = new ArrayList<>();

       for(int i=0; i<commentList.size(); i++){
           CommentDto.CommentReponseDto commentReponseDto = new CommentDto.CommentReponseDto(commentList.get(i).getUsername(), commentList.get(i).getComment(), commentList.get(i).getLikeCount());
           commentDtoList.add(commentReponseDto);
       }
       return commentDtoList;
    }

    @Transactional
    public void wirteComment(Long boardId, CommentDto.CommentRequestDto commentRequestDto, UserDetails userDetails) {

        Board board = boardRepository.findBoardById(boardId).orElseThrow(()-> new IllegalArgumentException("존재하지 않는 게시글 입니다."));

        commentRepository.saveAndFlush(new Comment(userDetails.getUsername(), commentRequestDto.getComment(), board));
    }

    @Transactional
    public CommentDto.CommentReponseDto commentModify(Long commentId, CommentDto.CommentRequestDto commentRequestDto, UserDetails userDetails){

        //존재하는 글인지 확인
        Comment comment = commentRepository.findById(commentId)
                                .orElseThrow(()-> new IllegalArgumentException("존재하는 댓글이 아닙니다."));

        //작성자가 맞는지 확인
        if(!comment.getUsername().equals(userDetails.getUsername())){
            throw new IllegalArgumentException("작성자가 일치하지 않습니다.");
        }
        comment.update(userDetails.getUsername(), commentRequestDto.getComment());

        Comment modifyComment = commentRepository.findById(comment.getId())
                                .orElseThrow(()-> new IllegalArgumentException("존재하는 댓글이 아닙니다."));

        return new CommentDto.CommentReponseDto(modifyComment.getUsername(), modifyComment.getComment(), modifyComment.getLikeCount());
    }

    @Transactional
    public void commentDelete(Long commentId, UserDetails userDetails) {
        //존재하는 글인지 확인
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(()-> new IllegalArgumentException("존재하는 댓글이 아닙니다."));

        //작성자가 맞는지 확인
        if(!comment.getUsername().equals(userDetails.getUsername())){
            throw new IllegalArgumentException("작성자가 일치하지 않습니다.");
        }
        commentRepository.delete(comment);
    }
}
