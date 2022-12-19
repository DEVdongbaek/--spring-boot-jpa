package com.jpabook.jpashop.domain;

import com.jpabook.jpashop.domain.Item.Item;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Category {

    @Id @GeneratedValue
    @Column(name = "category_id")
    private Long id;

    private String name;

    // ManyToMany는 최대한 피하는게 좋고, 중간 테이블을 만들어주는 게 맞다.
    // ex) 카테고리 - 카테고리&아이템 - 아이템
    // 다대다관계는 @JoinTable을 통해서 중간 테이블을 만들어주어야한다.
    // @JoinTable은 각 테이블들의 PK값들을 인자로 받아서, 중계 테이블을 만들어준다.
    // 실전에서 사용하지않는 이유는 중간 테이블에 각 테이블 PK 말고는 값을 추가할 수 없기 때문이다.
    @ManyToMany
    @JoinTable(name = "category_item",
            joinColumns = @JoinColumn(name = "category_id"), // joinColumns는 중간 테이블에 있는 category_id
            inverseJoinColumns = @JoinColumn(name = "item_id")) // inverseJoinColumns는 중간 테이블에 있는 item_id
    private List<Item> items = new ArrayList<>();

    // Category 구조는 같은 엔티티 내의 계층구조이다. 카테고리(부모) 1 : N 카테고리(자식)
    // 부모는 하나이니까 ManyToOne
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent;

    // 자식은 여러개니까 OneToMany
    @OneToMany(mappedBy = "parent") //
    private List<Category> child = new ArrayList<>();

    //==연관관계 메소드==//
    public void addChildCategory(Category child) {
        this.child.add(child);
        child.setParent(this);
    }
}
