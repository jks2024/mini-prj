package com.human.mini_prj.repository;

import com.human.mini_prj.constant.ItemSellStatus;
import com.human.mini_prj.entity.Item;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@SpringBootTest
class ItemRepositoryTest {
    @Autowired
    private ItemRepository itemRepository;  // 필드를 통한 의존성 주입

    @Test
    @DisplayName("상품 저장 테스트")  // 테스트 이름
    public void createItemTest() {
        for(int i = 1; i <= 10; i++) {
            Item item = new Item();
            item.setItemNm("테스트 상품" + i);
            item.setPrice(1000 * i);
            item.setItemDetail("테스트 상품 상세 설명" + i);
            item.setItemSellStatus(ItemSellStatus.SELL);
            item.setStockNumber(100);
            item.setRegTime(LocalDateTime.now());
            item.setUpdateTime(LocalDateTime.now());
            Item saveItem = itemRepository.save(item);
            log.info("Item 저장 : {}", saveItem);
        }
    }
    @Test
    @DisplayName("상품 조회 테스트")
    public void findByItemNumTest() {
        this.createItemTest();  // 10개의 상품을 생성
        List<Item> itemList = itemRepository.findByItemNm("테스트 상품5");
        for(Item item : itemList) {
            log.info("상품 조회 테스트 : {}", item);
        }
    }



}