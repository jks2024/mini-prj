package com.human.mini_prj.controller;

import com.human.mini_prj.dto.CommentResDto;
import com.human.mini_prj.dto.CommentWriteReqDto;
import com.human.mini_prj.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    // 게시글별 댓글 목록 조회
    @GetMapping("/list/{boardId}")
    public ResponseEntity<List<CommentResDto>> commentList(@PathVariable Long boardId) {
        List<CommentResDto> list = commentService.getCommentList(boardId);
        return ResponseEntity.ok(list);
    }

    // 댓글 작성
    @PostMapping("/write")
    public ResponseEntity<Boolean> commentWrite(@RequestBody CommentWriteReqDto dto) {
        boolean result = commentService.writeComment(dto);
        return ResponseEntity.ok(result);
    }

    // 댓글 수정
    @PutMapping("/{commentId}")
    public ResponseEntity<Boolean> commentModify(@PathVariable Long commentId, @RequestBody CommentWriteReqDto dto) {
        boolean result = commentService.updateComment(commentId, dto);
        return ResponseEntity.ok(result);
    }

    // 댓글 삭제
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Boolean> commentDelete(@PathVariable Long commentId) {
        boolean result = commentService.deleteComment(commentId);
        return ResponseEntity.ok(result);
    }
}
