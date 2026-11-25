package com.human.mini_prj.constant;

// 게시글 색인 상태: READY 인 글만 RAG 검샏 대상(허용목록)에 들어 감.
public enum IndexStatus {
    INDEXING, // 색인 대기, 진행 중 (작성, 수정 직 후)
    READY,    // 색인 완료 -> 검색 가능
    FAILED    // 색인 실패 -> 재색인으로 복구
}
