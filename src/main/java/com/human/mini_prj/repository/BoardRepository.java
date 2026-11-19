package com.human.mini_prj.repository;

import com.human.mini_prj.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {
    List<Board> findAllByOrderByRegTimeDesc();
    // 제목 또는 내용에 키워드가 포함된 게시글 (페이지네이션)
    // SQL : where title like %?% or content like %?%
    Page<Board> findByTitleContainingOrContentContaining(String title, String content, Pageable pageable);

    @Query("select b.id from Board b order by b.id")  // JPQL 쿼리 사용, 쿼리메서드 규칙이 없음
    List<Long> findAllIds();

}
