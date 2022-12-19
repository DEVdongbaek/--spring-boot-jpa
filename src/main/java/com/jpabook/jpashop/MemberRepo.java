package com.jpabook.jpashop;

import com.jpabook.jpashop.domain.Member;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class MemberRepo {

    @PersistenceContext
    private EntityManager em; // @PersistenceContext를 등록해놓으면 스프링이 알아서 엔티티매니저를 생성해준다.

    public Long save(Member member) {
        em.persist(member); // 멤버 엔티티 영속 컨텍스트에 저장 -> DB로 전달
        return member.getId();
    }

    public Member find(Long id) {
        return em.find(Member.class, id);
    }
}
