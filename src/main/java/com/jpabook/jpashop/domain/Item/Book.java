package com.jpabook.jpashop.domain.Item;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("B") // 싱글 테이블이기에, DB에서는 자식 테이블들을 분리할 기준이 필요하다.
@Getter @Setter
public class Book extends Item {

    private String author;
    private String isbn;
}
