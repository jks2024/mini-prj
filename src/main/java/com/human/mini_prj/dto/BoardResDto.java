package com.human.mini_prj.dto;

import com.human.mini_prj.constant.IndexStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class BoardResDto {
    private Long boardId;
    private String title;
    private String content;
    private String email;
    private String name;
    private int viewCount;
    private LocalDateTime regDate;
    private IndexStatus indexStatus;  // 색인 상태
    private String indexError;        // 색인 실패 사유
}
