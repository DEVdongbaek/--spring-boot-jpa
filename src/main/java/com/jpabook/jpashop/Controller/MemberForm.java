package com.jpabook.jpashop.Controller;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter @Setter
public class MemberForm {

    @NotEmpty(message = "회원 이름은 필수입니다.") // 값이 입력되지 않았을 때 오류를 전달한다.
    private String name;

    private String city;
    private String street;
    private String zipcode;
}
