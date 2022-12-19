package com.jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter // Lombok을 사용하기에 게터 세터 어노테이션 가능
public class Member {

    @Id @GeneratedValue
    // 엔티티는 타입(ex:Meber)가 있으므로, id 필드만으로 구별이 쉽지만, DB의 경우 타입이 없기에 id라고 이름을 지으면 구별하기 어렵다.
    @Column(name = "member_id") // 보통 '테이블명+id'를 많이 사용한다. member_id라는 컬럼에 매핑한다.
    private Long id;

    private String name;

    @Embedded // 내장 타입을 포함했다는 의미
    private Address address;

    // 양방향 연관관계에서 주인이 아닌 쪽(1)은 MappedBy 속성을 넣어주고, 값은 연관 관계 주인인 Order.member
    // mappedBy를 지정해주면, 읽기 전용 필드가 되고, 값을 생성, 변경할 수 없다.
    // Order 테이블에 있는 member 필드에 의해서 나는 매핑된거야!
    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>();
}
