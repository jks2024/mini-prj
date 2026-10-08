package com.human.mini_prj.service;

import com.human.mini_prj.dto.MemberResDto;
import com.human.mini_prj.dto.MemberUpdateReqDto;
import com.human.mini_prj.entity.Member;
import com.human.mini_prj.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;

@Slf4j          // log 메시지
@Service        // Spring Container에 Bean 등록
@Transactional  // 여러개의 물리적이 작업을 논리적인 작업으로 묶음
@RequiredArgsConstructor  // 생성자를 통한 의존성 주입을 자동으로 만들어 줌
public class MemberService {
    private final MemberRepository memberRepository;  // MemberRepository를 의존성 주입 받음

    // 회원 전체 조회
    @Transactional(readOnly = true)  // 조회 전용: 변경 감지(스냅샷) 생략 → 성능 이점
    public List<MemberResDto> getMemberList() {
        List<Member> memberList = memberRepository.findAll();
        List<MemberResDto> memberResDtoList = new ArrayList<>();
        for (Member member : memberList) {
            memberResDtoList.add(toDto(member));
        }
        return memberResDtoList;
    }

    // 회원 상세 조회
    @Transactional(readOnly = true)
    public MemberResDto getMemberDetail(String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("해당 회원이 존재하지 않습니다: " + email));
        return toDto(member);
    }

    // 회원 수정
    public boolean updateMember(MemberUpdateReqDto dto) {
        try {
            Member member = memberRepository.findByEmail(dto.getEmail())
                    .orElseThrow(() -> new IllegalArgumentException("해당 회원이 존재하지 않습니다: " + dto.getEmail()));
            member.setName(dto.getName());
            member.setImage(dto.getImage());
            // save() 호출 불필요: 영속 상태의 엔티티는 트랜잭션 커밋 시 변경 감지(Dirty Checking)로 UPDATE 실행
            return true;
        } catch (Exception e) {
            log.error("회원 정보 수정 시 오류 발생: {}", e.getMessage());
            return false;
        }
    }

    // 회원 삭제
    public boolean deleteMember(String email) {
        try {
            Member member = memberRepository.findByEmail(email)
                    .orElseThrow(() -> new IllegalArgumentException("해당 회원이 존재하지 않습니다: " + email));
            memberRepository.delete(member);
            return true;
        } catch (Exception e) {
            log.error("회원 삭제 시 오류 발생: {}", e.getMessage());
            return false;
        }
    }

    // Entity를 DTO로 변환하는 메서드 (pwd는 제외)
    private MemberResDto toDto(Member member) {
        MemberResDto dto = new MemberResDto();
        dto.setId(member.getId());
        dto.setEmail(member.getEmail());
        dto.setName(member.getName());
        dto.setImage(member.getImage());
        dto.setRegDate(member.getRegDate());
        return dto;
    }
}
