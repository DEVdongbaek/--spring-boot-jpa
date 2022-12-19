package com.jpabook.jpashop.repository;

import com.jpabook.jpashop.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    private final EntityManager em;

    public void save(Member member){
        em.persist(member);
    }

    public Member findOne(Long id) {
        return  em.find(Member.class, id); // .find()는 첫번째 인자로 타입, 두번째 인자로 PK를 넣어주면 된다.
    }

    public List<Member> findAll(){
        return em.createQuery("select m from Member m", Member.class) // 첫번째 인자가 jpql, 두번째 인자가 반환 타입
                .getResultList(); // ctrl + alt + N = 리턴문과 변수 선언을 합쳐준다.
    }

    public List<Member> findByName(String name) {
        return em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name", name) // where 문의 파라미터를 인자로 받은 name으로 설정해준다.
                .getResultList(); // 결과를 컬렉션으로 반환한다.
    }
}
