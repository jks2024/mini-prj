package com.human.mini_prj.rag;

import com.human.mini_prj.constant.IndexStatus;
import com.human.mini_prj.entity.Board;
import com.human.mini_prj.rag.dto.IndexReqDto;
import com.human.mini_prj.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

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
}
