package com.human.mini_prj.rag;

import com.human.mini_prj.constant.IndexStatus;
import com.human.mini_prj.rag.dto.QueryResDto;
import com.human.mini_prj.rag.dto.RagChatReqDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rag")
@RequiredArgsConstructor
public class RagChatController {
    private final RagChatService ragChatService;
    private final BoardIndexService boardIndexService;

    @PostMapping("/chat")
    public ResponseEntity<QueryResDto> chat(@RequestBody RagChatReqDto req) {
        return ResponseEntity.ok(ragChatService.ask(req));
    }
    // 실패한 글 하나 다시 색인
    @PostMapping("/reindex/{boardId}")
    public ResponseEntity<IndexStatus> reindex(@PathVariable Long boardId) {
        return ResponseEntity.ok(boardIndexService.index(boardId));
    }
}
