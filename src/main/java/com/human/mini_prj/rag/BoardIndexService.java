package com.human.mini_prj.rag;

import com.human.mini_prj.constant.IndexStatus;
import com.human.mini_prj.entity.Board;
import com.human.mini_prj.rag.dto.IndexReqDto;
import com.human.mini_prj.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.util.StringUtils.truncate;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardIndexService {  // 색인, 삭제
    private final BoardRepository boardRepository;
    private final RagClient ragClient;

    public IndexStatus index(Long boardId) {
        Board board = boardRepository.findWithMemberById(boardId).orElse(null);
        if (board == null) {
            log.warn("[RAG] 색인 대상 게시글이 없습니다. {}", boardId);
            return null;
        }
        boardRepository.updateIndexStatus(boardId, IndexStatus.INDEXING, null, null);
        try {
            IndexReqDto req = new IndexReqDto(
                    board.getId(),
                    board.getTitle(),
                    board.getContent() == null ? "" : board.getContent(),
                    board.getMember().getName(),     // 카카오 회원은 닉네임
                    board.getMember().getEmail());   // 카카오 회원은 kakao_회원번호
            int chunks = ragClient.index(req);
            boardRepository.updateIndexStatus(boardId, IndexStatus.READY, null, LocalDateTime.now());
            log.info("[RAG] 색인 완료: board={} chunks={}", boardId, chunks);
            return IndexStatus.READY;
        } catch (Exception e) {
            String reason = truncate(e.getMessage(), 500);
            boardRepository.updateIndexStatus(boardId, IndexStatus.FAILED, reason, null);
            log.error("[RAG] 색인 실패: board={} reason={}", boardId, reason);
            return IndexStatus.FAILED;
        }
    }
    public void remove(Long boardId) {
        try {
            ragClient.delete(boardId);
            log.info("[RAG] 청크 삭제 완료: board={}", boardId);
        } catch (Exception e) {
            log.warn("[RAG] 청크 삭제 실패(허용목록으로 차단됨): board={} reason={}", boardId, e.getMessage());
        }
    }

    // 전체 재색인: 스텁 엔진 재시작 후, Atlas 로 엔진을 바꾼 뒤, 기존 글을 처음 색인할 때
    public int reindexAll() {
        List<Long> ids = boardRepository.findAllIds();
        int ready = 0;
        for (Long id : ids) {
            if (index(id) == IndexStatus.READY) ready++;
        }
        log.info("[RAG] 전체 재색인: {}건 중 {}건 READY", ids.size(), ready);
        return ready;
    }

    private String truncate(String s, int max) {
        if (s == null) return "알 수 없는 오류";
        return s.length() <= max ? s : s.substring(0, max);
    }
}
