package com.sangwook.shoppingmall.domain.itemImage;

import com.sangwook.shoppingmall.domain.item.Item;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class ItemImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "itemId")
    private Item item;

    private String imageLink;

    private Integer imageNumber;

    private ItemImage(Item item, String imageLink, Integer imageNumber) {
        this.item = item;
        this.imageLink = imageLink;
        this.imageNumber = imageNumber;
    }

    public ItemImage() {

    }

    public static List<ItemImage> addImage(List<String> imageLinks, Item item) {
        List<ItemImage> image = new ArrayList<>();

        for (int i = 0; i < imageLinks.size(); i++) {
            image.add(new ItemImage(item, imageLinks.get(i), i + 1));
        }
        return image;
    }
}
