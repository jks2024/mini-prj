package com.human.mini_prj.controller;

import com.human.mini_prj.dto.*;
import com.human.mini_prj.security.SecurityUtil;
import com.human.mini_prj.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController  // Restfull API, 직렬화/역질렬화 기능 제공
@RequestMapping("/auth")  // http://localhost:8111/auth/
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
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
    public ResponseEntity<TokenDto> login(@RequestBody LoginReqDto dto) {
        return ResponseEntity.ok(authService.login(dto.getEmail(), dto.getPwd()));
    }
    // 재발급
    @PostMapping("/reissue")
    public ResponseEntity<TokenDto> reissue(@RequestBody ReissueReqDto dto) {
        return ResponseEntity.ok(authService.reissue(dto.refreshToken()));
    }
    // 로그아웃
    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        authService.logout(SecurityUtil.currentEmail());
        return ResponseEntity.noContent().build();
    }
    // 카카오 로그인: 인가 코드 -> 우리 서비스 토큰 발행
    @PostMapping("/kakao")
    public ResponseEntity<TokenDto> kakaoLogin(@RequestBody KakaoLoginReqDto dto) {
        return ResponseEntity.ok(authService.kakaoLogin(dto.code()));
    }
}
