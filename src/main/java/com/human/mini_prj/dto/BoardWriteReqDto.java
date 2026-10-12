package com.human.mini_prj.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BoardWriteReqDto {  // 게시글 작성
    private String title;
    private String content;
    private String email;  // 작성자가 누구인지 찾기 위해서
}
