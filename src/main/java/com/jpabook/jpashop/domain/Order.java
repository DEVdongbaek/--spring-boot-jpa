package com.jpabook.jpashop.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders") // 테이블 이름을 지정하지않으면 자동으로 Order
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order {

    @Id @GeneratedValue
    @Column(name = "order_id") // DB 컬럼 명을 DB이름_필드이름
    private Long id;

    // 회원(1) : 주문(N)이기에, 주문(N)Many에 to 회원 외래키(FK)One를 둔다.
    @ManyToOne(fetch = FetchType.LAZY) // FetchType.Lazy는 JPA의 프록시와 지연로딩의 기초적인 내용을 공부해야한다.
    @JoinColumn(name = "member_id") // FK이름 지정
    private Member member; // Order.member

    // 양방향 연관관계 -> 양쪽에 모두 서로의 외래키가 존재하는 경우
    // Db는 외래키 하나만 바꾸면 되지만, 객체 지향은 둘 다 바꿔줘야 한다. 이 간극을 해결하기 위해서 객체 지향에서 둘 중 하나를 주인으로 정한다.
    // 이 경우는 연관관계 주인을 정해주어야 한다. 그 이유는 Order.member를 통해 값을 바꿀 수도있고, Member.orders를 통해 값을 바꿀 수 있기에
    // 예를 들어 Order.member는 값을 변경했는데, 연관된 Member.orders는 값이 변경되지 않은 경우 둘 중 뭘 믿어야할지 JPA는 어려워함
    // 그래서 둘 중 N측(Order)을 주인으로 설정한다.
    // 그렇게하면 Order.member를 변경했을 경우, JPA는 Order.member를 변경했다는 것을 인지하고, 그 것을 중점으로 DB 업데이트를 한다.


    // 양방향 연관관계에서 주인이 아닌 쪽(1)은 MappedBy 속성을 넣어주고, 값은 연관 관계 주인인 OrderItem.order
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL) // 부모 엔티티가 영속화될 때 자식 엔티티도 같이 영속화되고, 부모 엔티티가 삭제될 때 자식 엔티티도 삭제되는 등 특정 엔티티를 영속 상태로 만들 때 연관된 엔티티도 함께 영속 상태로 전이되는 것을 의미합니다.
    // cascade = CascadeType.ALL를 통해서 orderItems에 데이터를 넣어두고,
    private List<OrderItem> orderItems = new ArrayList<>();

    /* 기존 방식 = 1. Order 엔티티 저장하고 2. 그다음 Order.OrderItem들을 엔티티당 각각 persist를 호출해야 했음.\
        3. 그다음 마지막으로 다시 Order를 Persist 해줘야함.
     persist(orderItemA)
     persist(orderItemB)
     persist(orderItemC)
     persist(order)
     */

    /* 현재 방식 = Casacade.All은 persist를 전파시켜주어서(영속성 전이), Order를 persist하면 Order.orderItems를 같이 persist해준다.
        삭제 또한 마찬가지로 같이 진행해준다.
    persist(order)
     */

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    // Order.delivery또한 Order가 persist될 때 같이 persist된다.
    @JoinColumn(name = "delivery_id")
    private Delivery delivery; // 배송 정보

    private LocalDateTime orderDate; // 주문 시간
    
    @Enumerated(EnumType.STRING)
    private OrderStatus status; // 주문상태 [ORDER. CANCEL]

    //==연관 관계 메소드==//
    /*
    양방향 관계의
    Order랑 Member가 있을 때, Member가 주문하면 Member.orders에도 넣어주어야한다.
    그리고 Order.member에도 저장을 해야한다.

     */

    public void setMember(Member member){ // 주문한 멤버를 세팅할 때,
        this.member = member; // Order.member에 멤버를 넣어준다.
        member.getOrders().add(this); // Order.member.orders에도 해당 주문을 넣어준다. 즉 this는 Order 객체를 가리킨다.
    }

    public void addOrderItem(OrderItem orderItem){
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
        delivery.setOrder(this);
    }


    //==생성 메서드 && 정적 팩토리 메서드 ==//
    public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems){ // OrderItem은 가변인자 OrderItem의 인자가 0부터 여러개까지 올 수 있음
        Order order = new Order();
        order.setMember(member);
        order.setDelivery(delivery);

        for (OrderItem orderItem : orderItems) { // orderItems에 있는 인자들을 orderItem에 각각 반복시킴
            order.addOrderItem(orderItem);
        }

        order.setStatus(OrderStatus.ORDER);
        order.setOrderDate(LocalDateTime.now());

        return order;
    }

    //==비즈니스 로직==//
    /*
    주문 취소
     */
    public void cancle() {
        if (delivery.getStatus() == DeliveryStatus.COMP) { // 이미 배송 상태가 완료 상태라면
            throw new IllegalStateException("이미 배송완료된 상품은 취소가 불가능합니다.");
        }

        this.setStatus(OrderStatus.CANCEL); // 배송 상태가 완료가 아니라면 주문을 취소 시켜준다.
        for (OrderItem orderItem : orderItems) {
            orderItem.cancel(); // 고객이 주문한 상품들도 취소를 시켜준다.
        }
    }


    //==조회 로직==//
    /*
    전체 주문 가격 조회
     */
    public int getTotalPrice(){
        int totalPrice = 0;

        for (OrderItem orderItem : orderItems){ // Alt + Enter로 람다식으로 변환 가능
            // 주문 가격과 주문 수량이 OrderItem에 있다.
            totalPrice += orderItem.getTotalPrice(); // 주문할 때 주문 가격과 주문 수량을 곱해주어야 한다.
        }

        return totalPrice;
    }


}

