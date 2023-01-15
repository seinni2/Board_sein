package com.sein.service;

import com.sein.dto.BoardDto;
import com.sein.dto.CommentDto;
import com.sein.entity.Board;
import com.sein.entity.Comment;
import com.sein.entity.User;
import com.sein.repository.BoardRepository;
import com.sein.repository.CommentRepository;
import com.sein.repository.UserRepository;
import com.sein.security.UserDetailImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardSerivce {

    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<BoardDto.BoardResponseDto> getBoardAllList() {
        List<Board> boardList = boardRepository.findAllByOrderByModifiedAtDesc();
        List<BoardDto.BoardResponseDto> boardListDto = new ArrayList<>();

        for (int i = 0; i < boardList.size(); i++) {
            BoardDto.BoardResponseDto boardDto = new BoardDto.BoardResponseDto(boardList.get(i).getUsername(),
                    boardList.get(i).getTitle(), boardList.get(i).getContent(), boardList.get(i).getLikeCount());
            boardListDto.add(boardDto);
        }
        return boardListDto;
    }

    @Transactional
    public void boardWriter(BoardDto.BoardRequestDto boardRequestDto, UserDetailImpl userDetail) {

        User user = userRepository.findByUsername(userDetail.getUsername()).orElseThrow(() -> new IllegalArgumentException("요청하신 회원을 찾을 수 없습니다."));

        Board board = new Board(user, boardRequestDto.getTitle(), boardRequestDto.getContent());
        boardRepository.saveAndFlush(board);
    }

    @Transactional(readOnly = true)
    public BoardDto.BoardResponseDto getBoard(Long boardId) {
        Board board = boardRepository.findBoardById(boardId).orElseThrow(() -> new IllegalArgumentException("게시물이 존재하지 않습니다."));
        return new BoardDto.BoardResponseDto(board.getUsername(), board.getTitle(), board.getContent(), board.getLikeCount());
    }

    @Transactional
    public BoardDto.BoardResponseDto boardUpdate(Long boardId, BoardDto.BoardRequestDto boardRequestDto, UserDetailImpl userDetail) {
        Board board = boardRepository.findBoardById(boardId).orElseThrow(() -> new IllegalArgumentException("게시물이 존재하지 않습니다"));

        if (!userDetail.getUsername().equals(board.getUsername())) {
            throw new IllegalArgumentException("게시글 작성자가 아닙니다.");
        }

        board.update(boardRequestDto.getTitle(), boardRequestDto.getContent());
        return new BoardDto.BoardResponseDto(userDetail.getUsername(), board.getTitle(), board.getContent(), board.getLikeCount());
    }

    @Transactional
    public void boardDelete(Long boardId, UserDetailImpl userDetail) {
        Board board = boardRepository.findBoardById(boardId).orElseThrow(() -> new IllegalArgumentException("게시물이 존재하지 않습니다."));

        if (!userDetail.getUsername().equals(board.getUsername())) {
            throw new IllegalArgumentException("게시글 작성자가 아닙니다.");
        }
        boardRepository.delete(board);
    }
}
