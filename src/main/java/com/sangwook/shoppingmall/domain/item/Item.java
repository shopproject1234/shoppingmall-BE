package com.sangwook.shoppingmall.domain.item;

import com.sangwook.shoppingmall.constant.Category;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Category category;

    private String name;

    private Integer price;

    private Integer reviewCount;

    private Float reviewAverage;

    private Integer sales;

    private Float score;


}
