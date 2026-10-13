package com.human.mini_prj.controller;

import com.human.mini_prj.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    // 게시글별 댓글 목록 조회

    // 댓글 작성

    // 댓글 수정

    // 댓글 삭제
}
