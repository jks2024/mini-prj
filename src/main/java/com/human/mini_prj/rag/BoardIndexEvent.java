package com.human.mini_prj.rag;

// "게시글이 바뀌었다"는 사실만 담은 이벤트 (실제 색인은 커밋 이후 리스너 담당)
public record BoardIndexEvent(Action action, Long boardId) {
    public enum Action { UPSERT, DELETE}
    public static BoardIndexEvent upsert(Long id) { return new BoardIndexEvent(Action.UPSERT, id); }
    public static BoardIndexEvent delete(Long id) { return new BoardIndexEvent(Action.DELETE, id); }
}
