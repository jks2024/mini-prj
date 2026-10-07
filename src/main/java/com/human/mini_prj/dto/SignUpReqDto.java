package com.human.mini_prj.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// 회원 가입 요청 DTO
@Setter
@Getter
@NoArgsConstructor
public class SignUpReqDto {
    private String email;
    private String pwd;
    private String name;
}
