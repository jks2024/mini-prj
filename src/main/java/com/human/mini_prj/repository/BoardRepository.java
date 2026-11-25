package com.human.mini_prj.repository;

import com.human.mini_prj.constant.IndexStatus;
import com.human.mini_prj.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {
    List<Board> findAllByOrderByRegTimeDesc();
    // 제목 또는 내용에 키워드가 포함된 게시글 (페이지네이션)
    // SQL : where title like %?% or content like %?%
    Page<Board> findByTitleContainingOrContentContaining(String title, String content, Pageable pageable);

    @Query("select b.id from Board b order by b.id")  // JPQL 쿼리 사용, 쿼리메서드 규칙이 없음
    List<Long> findAllIds();

    // [RAG] 커밋 이후(리스너)에는 작성자를 지연 로딩할 수 없으므로 join fetch 로 한 번에
    @Query("select b from Board b join fetch b.member where b.id = :id")
    Optional<Board> findWithMemberById(@Param("id") Long id);

    // [RAG] READY 허용목록
    @Query("select b.id from Board b where b.indexStatus = :status")
    List<Long> findIdsByIndexStatus(@Param("status") IndexStatus status);

    @Query("select b.id from Board b where b.indexStatus = :status and b.member.email = :email")
    List<Long> findIdsByIndexStatusAndEmail(@Param("status") IndexStatus status, @Param("email") String email);

    // [RAG] 색인 상태 변경
    // AFTER_COMMIT 리스너 안에서는 이미 커밋된 트랜잭션에 '참여'만 하고 다시 커밋되지 않으므로
    // 반드시 REQUIRES_NEW 로 새 트랜잭션을 열어야 UPDATE 가 반영됨
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Modifying(clearAutomatically = true)
    @Query("update Board b set b.indexStatus = :status, b.indexError = :error, b.indexedAt = :indexedAt where b.id = :id")
    int updateIndexStatus(@Param("id") Long id, @Param("status") IndexStatus status,
                          @Param("error") String error, @Param("indexedAt") LocalDateTime indexedAt);

}
