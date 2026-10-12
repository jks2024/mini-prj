package com.human.mini_prj.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class BoardResDto {
    private Long id;
    private String title;
    private String content;
    private String email;
    private String name;
    private LocalDateTime regDate;
}
