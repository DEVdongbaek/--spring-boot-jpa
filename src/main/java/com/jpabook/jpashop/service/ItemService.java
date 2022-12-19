package com.jpabook.jpashop.service;

import com.jpabook.jpashop.domain.Item.Book;
import com.jpabook.jpashop.domain.Item.Item;
import com.jpabook.jpashop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
// readOnly가 있으면, JPA의 조회 성능을 최적화 시켜준다.
// JPA에서 모든 데이터 변경은 트랜잭션 안에서 실행되어야 한다.
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    @Transactional // 데이터 변경 기능이 있으면, @Transactional을 따로 어노테이션 해줘야 함.
    public void saveItem(Item item){
        itemRepository.save(item);
    }

    @Transactional
    public void updateItem(Long itemId, int price, String name, int StockQuantity){ // Update Form에서 입력받은 값을 bookParam 인자로 전달
        Item findItem = itemRepository.findOne(itemId); // 기존에 존재하는 엔티티를 가져옴

        // 기존 값들을 입력받은 값으로 변경시켜준다.
        findItem.setPrice(price);
        findItem.setName(name);
        findItem.setStockQuantity(StockQuantity);

    }

    public List<Item> findItems(){
        return itemRepository.findAll();
    }

    public Item findOne(Long id) {
        return itemRepository.findOne(id);
    }
}
