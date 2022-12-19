package com.jpabook.jpashop.service;

import com.jpabook.jpashop.domain.Member;
import com.jpabook.jpashop.repository.MemberRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true) // readOnly가 있으면, JPA의 조회 성능을 최적화 시켜준다.  // JPA에서 모든 데이터 변경은 트랜잭션 안에서 실행되어야 한다.
@RequiredArgsConstructor // final이 있는 필드만을 상대로 생성자를 만들어준다. -> 생성자 의존성 주입에 최적화
public class MemberService {

    // 생성자가 하나 있는 경우는 @Autowired를 생략해도 괜찮다.
    private final MemberRepository memberRepository; // final은 컴파일 시점에 체크를 해줄 수 있기에 넣는걸 추천

    /*
        회원 가입
     */
    @Transactional // 데이터 변경 기능이 있으면, @Transactional을 따로 어노테이션 해줘야 함.
    public Long join(Member member){
        validateDuplicateMember(member); // 회원 가입시 중복 검사 문제가 있을 시 예외 호출
        memberRepository.save(member);
        return member.getId();
    }


    private void validateDuplicateMember(Member member) {
        // 예외
         List<Member> findMembers = memberRepository.findByName(member.getName()); // getter로 member 객체의 name property를 가져옴
         if (!findMembers.isEmpty()) { // 유저가 이미 존재하다면
            throw new IllegalStateException("이미 존재하는 회원입니다.");
         }
    }


    // 회원 전체 조회
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }


    // 회원 한명만 조회
    public Member findOne(Long memberId) {
        return memberRepository.findOne(memberId);
    }
    
}
