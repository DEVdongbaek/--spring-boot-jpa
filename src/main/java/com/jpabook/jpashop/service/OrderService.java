package com.jpabook.jpashop.service;

import com.jpabook.jpashop.domain.Delivery;
import com.jpabook.jpashop.domain.Item.Item;
import com.jpabook.jpashop.domain.Member;
import com.jpabook.jpashop.domain.Order;
import com.jpabook.jpashop.domain.OrderItem;
import com.jpabook.jpashop.repository.ItemRepository;
import com.jpabook.jpashop.repository.MemberRepository;
import com.jpabook.jpashop.repository.OrderRepository;
import com.jpabook.jpashop.repository.OrderSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    // OrderService의 경우 의존하는 레포지토리가 많다.
    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    // 주문
    /*
    비즈니스 로직을 엔티티에 대부분 작성했기에, 서비스에서는 엔티티에 필요한 요청을 위임하는 역할을 맡는다.
     */
    @Transactional
    public Long order(Long memberId, Long itemId, int count) {
        
        // 엔티티 조회 -> 엔티티에 필요한 요청을 위임하기 위한 인자값들을 선언한다.
        Member member = memberRepository.findOne(memberId);
        Item item = itemRepository.findOne(itemId);
        
        // 배송정보 생성 -> 위 인자값들을 엔티티에 작성했던 비즈니스 로직들에 맞게 전송한다.
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress()); // 배송 주소를 회원의 주소로 저장한다.

        // 주문상품 생성 -> 위 인자값들을 엔티티에 작성했던 비즈니스 로직들에 맞게 전송한다.
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);
        
        // 주문 생성 -> 위 인자값들을 엔티티에 작성했던 비즈니스 로직들에 맞게 전송한다.
        Order order = Order.createOrder(member, delivery, orderItem); // ctrl + space로 인자 자동 생성
        
        // 주문만 저장하면 되는 이유
        // Order을 Persist하면, Order에 있는 CasacadeType.ALL로 인해서 컬렉션으로 있는 orderItems와 Delivery 또한 한꺼번에 persist된다.
        orderRepository.save(order);

        return order.getId();
    }

    // 주문 취소
    @Transactional
    public void cancelOrder(Long orderId) {
        
        // 주문 엔티티 조회
        Order order = orderRepository.findOne(orderId);
        
        // 주문 취소
        order.cancle();
    }

    // 검색
    public List<Order> findOrders(OrderSearch orderSearch) {
        return orderRepository.findAll(orderSearch);
    }
}
