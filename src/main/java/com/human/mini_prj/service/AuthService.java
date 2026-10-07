package com.human.mini_prj.service;

import com.human.mini_prj.dto.SignUpReqDto;
import com.human.mini_prj.entity.Member;
import com.human.mini_prj.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j  // log 메시지 출력을 위해 사용
@Service  // Spring Container에 Bean 등록
@Transactional // 트랜잭션 처리: 여러개의 물리적인 작업 단위를 한개의 논리적인 단위로 묶음
@RequiredArgsConstructor  // 생성자를 통한 의존성 주입을 자동으로 만들어 줌
public class AuthService {
    private final MemberRepository memberRepository;  // 생성자를 통한 의존성 주입

    // 회원 가입 여부 확인
    public boolean isDuplicatedEmail(String email) {
        return memberRepository.existsByEmail(email);
    }

    // 회원 가입
    public boolean signUp(SignUpReqDto dto) {
        try {
            Member member = toEntity(dto);
            memberRepository.save(member);
            return true;
        } catch (Exception e) {
            log.error("회원 가입 시 오류 발생: {}", e.getMessage());
            return false;
        }
    }

    // 로그인
    public boolean login(String email, String pwd) {
        Optional<Member> member = memberRepository.findByEmailAndPwd(email, pwd);
        return member.isPresent();
    }

    // DTO -> Entity
    private Member toEntity(SignUpReqDto dto) {
        Member member = new Member();
        member.setName(dto.getName());
        member.setPwd(dto.getPwd());
        member.setEmail(dto.getEmail());
        return member;
    }
}
