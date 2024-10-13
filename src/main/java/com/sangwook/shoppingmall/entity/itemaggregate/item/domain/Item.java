package com.sangwook.shoppingmall.entity.itemaggregate.item.domain;

import com.sangwook.shoppingmall.constant.Category;
import com.sangwook.shoppingmall.entity.itemaggregate.item.child.review.domain.dto.ReviewWrite;
import com.sangwook.shoppingmall.entity.itemaggregate.item.domain.dto.AddItem;
import com.sangwook.shoppingmall.entity.useraggregate.user.domain.User;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;

    @Enumerated(EnumType.STRING)
    private Category category;

    private String name;

    private Integer price;

    private Integer itemCount;

    private int reviewCount;

    private float reviewAverage;

    private int sales;

    private float score;

    private LocalDateTime time;

    @Column(length = 1000)
    private String itemInfo;

    @OneToMany(mappedBy = "item")
    private List<ItemImage> images;

    @OneToMany(mappedBy = "item")
    private List<Review> reviews;

    public static Item add(AddItem addItem, User user) {
        Item item = new Item();
        item.category = addItem.getCategory();
        item.name = addItem.getItemName();
        item.price = addItem.getPrice();
        item.itemCount = addItem.getItemCount();
        item.user = user;
        item.itemInfo = addItem.getItemInfo();
        item.time = LocalDateTime.now();
        return item;
    }

    public void purchased(Integer count) {
        this.itemCount -= count;
        sales += count;
    }

    /**
     * 리뷰가 추가될 때 업데이트되는 Item 객체의 리뷰수와 점수
     */
    private void reviewAdded(float score) {
        if (this.reviewCount == 0) {
            this.reviewAverage = score;
        } else {
            this.reviewAverage = Math.round(((reviewAverage * reviewCount) + score) / (reviewCount + 1) * 100) / 100.0f;
        }

        this.reviewCount += 1;
    }

    private void reviewUpdated(float oldScore, float newScore) {
        this.reviewAverage = Math.round(((reviewAverage * reviewCount) - oldScore) / (reviewCount - 1) * 100) / 100.0f;
        this.reviewCount -= 1;
        this.reviewAverage = Math.round(((reviewAverage * reviewCount) + newScore) / (reviewCount + 1) * 100) / 100.0f;
        this.reviewCount += 1;
    }

    /**
     * ItemImage
     */
    public List<ItemImage> addImage(List<String> imageLinks) {
        List<ItemImage> image = new ArrayList<>();

        for (int i = 0; i < imageLinks.size(); i++) {
            ItemImage itemImage = new ItemImage(this, imageLinks.get(i), i + 1);
            image.add(itemImage);
            images.add(itemImage);
        }
        return image;
    }

    /**
     * Review
     * 사실 Item과 Review를 묶지 않는 편이 나을 수도 있지만
     * Item이라는 root entity안에 2개 이상의 엔티티를 넣어 구현하고 싶었다
     */
    public Review write(User user, ReviewWrite reviewWrite) {
        Review review = new Review(user, this, reviewWrite.getContent(), reviewWrite.getScore());
        reviewAdded(reviewWrite.getScore());
        reviews.add(review);
        return review;
    }

    /**
     * 엄밀히 말하면 DDD의 규칙에 어긋나는 코드이다
     * 이 메서드는 Review 객체를 외부에서 가져오고 있다
     * 하지만 Item 객체 안에서 getReviewWithUser()메서드를 사용하여 Review를 찾는것이
     * 더 비효율적이라고 생각되어 이렇게 구현하였다
     */
    public Review update(Review review, ReviewWrite reviewWrite) {
        Review update = review.update(reviewWrite.getContent(), reviewWrite.getScore());
        reviewUpdated(review.getScore(), reviewWrite.getScore());
        return update;
    }

    /**
     * Item 객체안의 특정 유저의 Review를 찾아오는 메서드이다
     * 하지만 예를들어 리뷰가 많이 달려서 1000개가 넘어가는 상황을 생각해보면
     * N+1 문제가 발생할 수 있는 이 메서드에서 반복문을 돌려서 1개의 특정 리뷰를 찾는것이란 너무 비효율적이다
     * 이 메서드는 사용하지는 않고 구현만 해놓을 예정이다
     * 특정 리뷰를 찾을 때는 repository를 사용하여 찾아오도록 할것이다
     */
    private Review getReviewWithUser(User user) {
        for (Review review : reviews) {
            if (review.getUser().equals(user)) {
                return review;
            }
        }
        throw new IllegalStateException();
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        Item item = (Item) object;

        return Objects.equals(id, item.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
