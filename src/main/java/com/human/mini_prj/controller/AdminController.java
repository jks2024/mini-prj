package com.human.mini_prj.controller;

import com.human.mini_prj.rag.BoardIndexService;
import com.human.mini_prj.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AuthService authService;
    private final BoardIndexService boardIndexService;

    // 탈취 신고 시 강제 로그아웃
    @DeleteMapping("/token/{email}")
    public ResponseEntity<Void> revoke(@PathVariable String email) {
        authService.revoke(email);
        return ResponseEntity.noContent().build();
    }

    // [RAG] 전체 재색인 (ROLE_ADMIN 만): 스텁 엔진 재시작 후 · Atlas 전환 후 · 기존 글 첫 색인
    @PostMapping("/rag/reindex")
    public ResponseEntity<Integer> reindexAll() {
        return ResponseEntity.ok(boardIndexService.reindexAll());
    }
}
