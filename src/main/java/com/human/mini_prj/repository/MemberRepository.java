package com.human.mini_prj.repository;


import com.human.mini_prj.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    // 기본적인 CRUD 는 상속을 통해서 만들어졌고, 이를 구현하는 구현체는 별도의 hibernate가 SQL 문으로 변경 해줌
    Optional<Member> findByEmail(String email);  // Sql : select * from member where email = ?
    boolean existsByEmail(String email); // Sql : select count(*) from member where email = ?
    Optional<Member> findByEmailAndPwd(String email, String pwd);  // Sql : select * from member where email = ? and pwd = ?

    // 1. 이름이 정확히 일치하는 회원 목록 조회

    // 2. 이름에 특정 글자가 포함된 회원 조회

    // 3. 특정 도메인(예: @gmail.com)으로 끝나는 이메일을 가진 회원 조회

    // 4. 이름 또는 이메일이 일치하는 회원 조회

    // 5. 특정 일시 이후에 가입한 회원 조회

    // 6. 두 일시 사이에 가입한 회원 조회

    // 7. 전체 회원을 가입일 최신순으로 조회

    // 8. 가장 최근에 가입한 회원 3명만 조회

    // 9. 대소문자 구분 없이 이름에 키워드가 포함된 회원을 이름 오름차순으로 조회

    // 10. 이메일로 회원 삭제
}
