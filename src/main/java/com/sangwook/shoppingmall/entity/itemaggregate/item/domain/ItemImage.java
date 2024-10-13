package com.sangwook.shoppingmall.entity.itemaggregate.item.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ItemImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "itemId")
    private Item item;

    private String imageLink;

    private Integer imageNumber;

    protected ItemImage(Item item, String imageLink, Integer imageNumber) {
        this.item = item;
        this.imageLink = imageLink;
        this.imageNumber = imageNumber;
    }
}
