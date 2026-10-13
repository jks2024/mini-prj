package com.human.mini_prj.service;

import com.human.mini_prj.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {
    private final BoardService boardRepository;
    private final MemberService memberRepository;
    private final CommentRepository commentRepository;

    // 게시글별 댓글 목록 조회

    // 댓글 작성

    // 댓글 수정

    // 댓글 삭제

    // Entity -> Dto
}
