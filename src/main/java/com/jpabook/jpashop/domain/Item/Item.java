package com.jpabook.jpashop.domain.Item;

import com.jpabook.jpashop.domain.Category;
import com.jpabook.jpashop.exception.NotEnoughStockException;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE) // 상속관계 전략을 부모 엔티티(클래스)에 해주어야한다.
// 싱글 테이블 전략시 부모 엔티티를 기준으로 자식 엔티티들이 모두 한 테이블에 생성된다.
@DiscriminatorColumn(name = "dtype") // type별로 구분하는 것
@Getter @Setter
public abstract class Item { // 추상 클래스(부모 클래스)

    @Id
    @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String name;

    private int price;

    private int stockQuantity;

    @ManyToMany(mappedBy = "items")
    private List<Category> cagegories = new ArrayList<Category>();


    //==비즈니스 로직==//
    // 도메인 주도 설계를 할 때, Entity 자체가 해결할 수 있는 문제는 Entity 안에 비즈니스 로직을 넣는게 좋다.
    // stockQuantity는 Item 엔티티 안에 있으니 Item에서 처리하는게 좋다. 자율성, 캡슐화 증가, 안전
    public void addStock(int quantity){
        this.stockQuantity += quantity; // 재고 수량을 증가하는 로직
    }

    public void removeStock(int quantity){
        int restStock =  this.stockQuantity -= quantity; // 재고 수량을 감소하는 로직
        if (restStock < 0){
            throw new NotEnoughStockException("need more stock"); // exception 클래스를 따로 만들어주자.
        }
        this.stockQuantity = restStock;
    }

}
