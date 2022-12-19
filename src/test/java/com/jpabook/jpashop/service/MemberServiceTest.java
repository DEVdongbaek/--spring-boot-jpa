package com.jpabook.jpashop.service;

import com.jpabook.jpashop.domain.Member;
import com.jpabook.jpashop.repository.MemberRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional // 이 어노테이션이 테스트에 있으면 데이터를 롤백 함 -> 테스트시 DB에 영향이 없게 한다.
public class MemberServiceTest {

    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;

    @Test
    public void 회원가입() throws Exception {
        //given <- 테스트시 주어질 데이터 
        Member member = new Member();
        member.setName("Sonny");

        //when <- 테스트시 실행될 기능
        Long savedId = memberService.join(member);

        //then <- 테스트가 실행되었을 때 기대하는 값
        assertEquals(member, memberRepository.findOne(savedId)); // 도메인에 직접 값을 넣은 것과 회원가입 기능을 이용한 것의 id 값이 같은지
    }

    @Test(expected = IllegalStateException.class)
    public void 중복_회원_예외() throws Exception {
        //given
        Member member1 = new Member();
        member1.setName("kim");

        Member member2 = new Member();
        member2.setName("kim");

        //when
        // 위 @Test(expected = IllegalStateException.class) 덕분에 아래와 같이 try, catch문을 안적어도 된다.
        memberService.join(member1);
        memberService.join(member2);

//        memberService.join(member1);
//        try {
//            memberService.join(member2); // 이미 "kim"이라는 이름을 가진 사람이 있기에 예외가 발생해야한다!!
//        } catch (IllegalStateException e) { // 만약 예외가 발생했으면 테스트 통과!
//            return;
//        }

        //then
        fail("예외가 발생해야 한다."); // fail은 위 코드들에서 예외가 터져야하는데 안터져서, 아래 코드까지 왔을 때의 에러 코드

    }



}