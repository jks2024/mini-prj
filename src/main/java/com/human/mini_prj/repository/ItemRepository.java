package com.human.mini_prj.repository;

import com.human.mini_prj.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {  // JPA 설계명세
    // 기본적인 CRUD를 모두 포함하고 있음

    // 상품명으로 상품 검색
    List<Item> findByItemNm(String itemNm);  // findBy + 변수 이름, sql : select * from item where item_nm = ?
}
