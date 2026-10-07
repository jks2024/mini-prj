package com.human.mini_prj.controller;

import com.human.mini_prj.dto.LoginReqDto;
import com.human.mini_prj.dto.SignUpReqDto;
import com.human.mini_prj.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController  // Restfull API, 직렬화/역질렬화 기능 제공
@RequestMapping("/auth")  // http://localhost:8111/auth/
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;  // 생성자를 통한 의존성 주입

    // 회원 가입 여부 확인
    @GetMapping("/exists/{email}")
    public boolean existsEmail(@PathVariable String email) {
        return authService.isDuplicatedEmail(email);
    }

    // 회원 가입
    @PostMapping("/signup")
    public ResponseEntity<Boolean> signUp(@RequestBody SignUpReqDto dto) {
        return ResponseEntity.ok(authService.signUp(dto));
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<Boolean> login(@RequestBody LoginReqDto dto) {
        return ResponseEntity.ok(authService.login(dto.getEmail(), dto.getPwd()));
    }
}
