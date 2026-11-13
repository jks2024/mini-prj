package com.human.mini_prj.service;

import com.human.mini_prj.dto.BoardResDto;
import com.human.mini_prj.dto.BoardWriteReqDto;
import com.human.mini_prj.dto.PageResDto;
import com.human.mini_prj.entity.Board;
import com.human.mini_prj.entity.Member;
import com.human.mini_prj.repository.BoardRepository;
import com.human.mini_prj.repository.CommentRepository;
import com.human.mini_prj.repository.MemberRepository;
import com.human.mini_prj.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    private final CommentRepository commentRepository;  // 게시글 삭제 전 댓글 정리

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
            String email = SecurityUtil.currentEmail();          // 토큰의 이메일
            Member member = memberRepository.findByEmail(email)
                    .orElseThrow(() -> new IllegalArgumentException("해당 회원이 존재하지 않습니다: " + email));
            Board board = new Board();
            board.setTitle(dto.getTitle());
            board.setContent(dto.getContent());
            board.setMember(member);
            boardRepository.save(board);
            return true;
        } catch (Exception e) {
            log.error("게시글 작성 시 오류 발생 : {}", e.getMessage());
            return false;
        }

    }

    // 게시글 수정
    public boolean updateBoard(Long id, BoardWriteReqDto dto) {
        try {
            Board board = boardRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다: " + id));
            checkWriter(board.getMember());
            board.setTitle(dto.getTitle());
            board.setContent(dto.getContent());
            return true;
        } catch (Exception e) {
            log.error("게시글 수정 시 오류 발생: {}", e.getMessage());
            return false;
        }

    }

    // 게시글 삭제: 게시글 id로 글 삭제
    public boolean deleteBoard(Long id) {
        try {
            Board board = boardRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다.: " + id));
            checkWriter(board.getMember());
            commentRepository.deleteByBoardId(id);   // 댓글(FK)을 먼저 지워야 게시글 삭제 가능
            boardRepository.delete(board);
            return true;
        } catch (Exception e) {
            log.error("게시글 삭제 시 오류 발생: {}", e.getMessage());
            return false;
        }
    }

    // 게시글 목록 페이지네이션 조회 (최신순)
    public PageResDto<BoardResDto> getBoardPage(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("regTime").descending());
        Page<Board> boardPage = boardRepository.findAll(pageable);
        Page<BoardResDto> boardResDtoPage = boardPage.map(this::toDto);
        return new PageResDto<>(boardResDtoPage);
    }

    // 제목 또는 내용에 키워드 포함
    public PageResDto<BoardResDto> searchBoard(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("regTime").descending());
        Page<Board> boardPage = boardRepository.findByTitleContainingOrContentContaining(keyword, keyword, pageable);
        Page<BoardResDto> boardResDtoPage = boardPage.map(this::toDto);
        return new PageResDto<>(boardResDtoPage);
    }

    // 로그인 사용자 == 작성자 확인
    private void checkWriter(Member writer) {
        if (!writer.getEmail().equals(SecurityUtil.currentEmail())) {
            throw new IllegalArgumentException("작성자만 수정/삭제할 수 있습니다.");
        }
    }

    // Entity -> Dto
    private BoardResDto toDto(Board board) {
        BoardResDto dto = new BoardResDto();
        dto.setBoardId(board.getId());
        dto.setTitle(board.getTitle());
        dto.setContent(board.getContent());
        dto.setEmail(board.getMember().getEmail());
        dto.setName(board.getMember().getName());
        dto.setRegDate(board.getRegTime());
        return dto;
    }

}

