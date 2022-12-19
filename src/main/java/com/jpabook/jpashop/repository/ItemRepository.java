package com.jpabook.jpashop.repository;

import com.jpabook.jpashop.domain.Item.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor // final이 있는 필드만을 상대로 생성자를 만들어준다. -> 생성자 의존성 주입에 최적화
public class ItemRepository {

    private final EntityManager em;

    public void save(Item item) {
        if (item.getId() == null){ // id가 중복이 안된다면 -> 새로운 객체라면
            em.persist(item);
        } else { // 이미 DB에 있다면
            em.merge(item); // 강제 업데이트 시켜준다.
        }
    }

    public Item findOne(Long id) {
        return em.find(Item.class, id);
    }

    public List<Item> findAll() {
        return em.createQuery("select i from Item i", Item.class)
                .getResultList();
    }

}
