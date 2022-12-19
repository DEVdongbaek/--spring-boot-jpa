package com.jpabook.jpashop.domain;

import lombok.Getter;

import javax.persistence.Embeddable;

@Embeddable // JPA Entity안의 Column을 하나의 객체로써 사용을 하고 싶을 때 사용, 어딘가에 내장될 수 있음을 알림
@Getter // 값 타입은 반드시 Getter만 사용하자. 그 이유는 생성될 때의 값이 변경되면 안되기 때문이다.
public class Address {

    // 다른 엔티티에서 Address안의 엔티티를 Address라는 하나의 객체로 사용할 수 있다.
    private String city;
    private String streets;
    private String zipcode;

    // public, protected중 protected로 설정하는 것을 추천
    protected Address() { // JPA가 생성할 때 @Embeddable가 있는 클래스는 리프랙션, 프록시등의 기술을 사용하기 위해서,기본 생성자를 요구한다.

    }

    // > 참고: 값 타입은 변경 불가능하게 설계해야 한다.
    //> @Setter 를 제거하고, 생성자에서 값을 모두 초기화해서 변경 불가능한 클래스를 만들자. JPA 스펙상 엔티
    // 티나 임베디드 타입( @Embeddable )은 자바 기본 생성자(default constructor)를 public 또는
    // protected 로 설정해야 한다. public 으로 두는 것 보다는 protected 로 설정하는 것이 그나마 더 안전
    // 하다.
    //> JPA가 이런 제약을 두는 이유는 JPA 구현 라이브러리가 객체를 생성할 때 리플랙션 같은 기술을 사용할 수있도록 지원해야 하기 때문이다
    public Address(String city, String streets, String zipcode) {
        this.city = city;
        this.streets = streets;
        this.zipcode = zipcode;
    }

}
