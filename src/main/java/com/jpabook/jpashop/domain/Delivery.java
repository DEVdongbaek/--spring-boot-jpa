package com.jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
public class Delivery {

    @Id @GeneratedValue
    @Column(name = "delivery_id")
    private Long id;

    // OneToOne 또한 반드시 연관관계의 주인을 정해주어야한다.
    // 이 때 주인은 어느쪽에서 더 많이 참조하는지를 보면서 정하면 된다. 예를 들어서
    // 유저가 주문.배송을 찾는 것과 배송.주문을 찾는 것중 주문.배송을 더 많이 찾기에 주문에 FK를 넣는다.
    @OneToOne(mappedBy = "delivery", fetch = FetchType.LAZY)
    private Order order;

    @Embedded
    private Address address;

    // Enum Type은 무조건 STRING으로 하자. ORDINAL로 할 경우 도중 데이터 변경시 장애가 생긴다.
    @Enumerated(EnumType.STRING)
    private DeliveryStatus status; // ENUM [READY(준비), COMP(배송)]

}
