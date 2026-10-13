package com.human.mini_prj.repository;

import com.human.mini_prj.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    // 게시글에 대한 댓글 목록 조회(최신순)
    List<Comment> findByBoardIdOrderByRegDateDesc(Long boardId);
}
