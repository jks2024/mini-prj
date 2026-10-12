package com.human.mini_prj.service;

import com.human.mini_prj.dto.BoardResDto;
import com.human.mini_prj.dto.BoardWriteReqDto;
import com.human.mini_prj.entity.Board;
import com.human.mini_prj.entity.Member;
import com.human.mini_prj.repository.BoardRepository;
import com.human.mini_prj.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class BoardService {
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

    // 게시글 전체 조회 (최신순)
    public List<BoardResDto> getBoardList() {
        List<Board> boardList = boardRepository.findAllByOrderByRegTimeDesc();
        List<BoardResDto> boardResDtoList = new ArrayList<>();
        for (Board board : boardList) {
            boardResDtoList.add(toDto(board));
        }
        return boardResDtoList;
    }


    // 게시글 상세 조회
    public BoardResDto getBoardDetail(Long id) {
        Optional<Board> board = boardRepository.findById(id);
        if (board.isEmpty()) {
            throw new IllegalArgumentException("해당 게시글이 존재하지 않습니다.");
        }
        return toDto(board.get());
    }

    // 게시글 작성
    public boolean writeBoard(BoardWriteReqDto dto) {
        try {
            // 작성자 조회
            Optional<Member> member = memberRepository.findByEmail(dto.getEmail());
            if (member.isEmpty()) {
                throw new IllegalArgumentException("해당 회원이 존재하지 않습니다: " + dto.getEmail());
            }
            Board board = new Board();
            board.setTitle(dto.getTitle());
            board.setContent(dto.getContent());
            board.setMember(member.get());
            boardRepository.save(board); // INSERT 와 동일
            return true;

        } catch (Exception e) {
            log.error("게시글 작성 시 오류 발생 : {}", e.getMessage());
            return false;
        }

    }

    // 게시글 수정

    // 게시글 삭제

    // Entity -> Dto
    private BoardResDto toDto(Board board) {
        BoardResDto dto = new BoardResDto();
        dto.setId(board.getId());
        dto.setTitle(board.getTitle());
        dto.setContent(board.getContent());
        dto.setEmail(board.getMember().getEmail());
        dto.setName(board.getMember().getName());
        dto.setRegDate(board.getRegTime());
        return dto;
    }


}

