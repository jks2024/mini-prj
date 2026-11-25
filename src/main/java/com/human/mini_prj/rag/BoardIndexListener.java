package com.human.mini_prj.rag;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class BoardIndexListener {
    private final BoardIndexService boardIndexService;

    // 게시글 트랜잭션이 커밋된 뒤에만 실행 (롤백되면 실행되지 않음)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onBoardChanged(BoardIndexEvent event) {
        log.info("[RAG] 게시글 변경 감지: {} {}", event.action(), event.boardId());
        switch (event.action()) {
            case UPSERT -> boardIndexService.index(event.boardId());
            case DELETE -> boardIndexService.remove(event.boardId());
        }
    }

}
