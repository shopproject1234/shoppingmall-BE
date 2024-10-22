package com.sangwook.shoppingmall.entity.itemaggregate.item.domain;

import com.sangwook.shoppingmall.constant.Category;
import com.sangwook.shoppingmall.entity.itemaggregate.item.child.review.domain.dto.ReviewWrite;
import com.sangwook.shoppingmall.entity.itemaggregate.item.domain.dto.AddItem;
import com.sangwook.shoppingmall.entity.useraggregate.user.domain.User;
import com.sangwook.shoppingmall.exception.custom.ObjectNotFoundException;
import com.sangwook.shoppingmall.exception.custom.UserValidationException;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.sangwook.shoppingmall.exception.MethodFunction.getMethodName;

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

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, orphanRemoval = true) //영속성 전이를 사용하여 image도 같이 관리한다
    private List<ItemImage> images = new ArrayList<>();

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();

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

    public Item update(AddItem addItem) {
        this.category = addItem.getCategory();
        this.name = addItem.getItemName();
        this.price = addItem.getPrice();
        this.itemCount = addItem.getItemCount();
        this.itemInfo = addItem.getItemInfo();
        updateImage(addItem.getImage());
        return this;
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

    private void reviewDeleted(float score) {
        this.reviewAverage = Math.round(((reviewAverage * reviewCount) - score) / (reviewCount - 1) * 100) / 100.0f;
        this.reviewCount -= 1;
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
     * 3개의 기존 이미지를 삭제하고 3개의 새로운 이미지를 추가하기위해서 삭제 쿼리 3개, 추가 쿼리 3개 발생
     * 이미지의 개수를 제한하는 방법으로 쿼리의 수를 제어할 수 있다
     */
    private void updateImage(List<String> imageLinks) {
        images.clear();
        /**
         * orphanRemoval = true로 인해 list에서 꺼내진 객체들은 삭제된다
         * 이때 실제 객체를 불러오는것이 아니기 때문에 fetch join으로 이미지를 가져오는 것은 의미가 없다
         */
        addImage(imageLinks);
    }

    /**
     * Review
     * 사실 Item과 Review를 묶지 않는 편이 나을 수도 있지만
     * Item이라는 root entity안에 2개 이상의 엔티티를 넣어 구현하고 싶었다
     */
    public Review writeReview(User user, ReviewWrite reviewWrite) {
        Review review = new Review(user, this, reviewWrite.getContent(), reviewWrite.getScore());
        reviewAdded(reviewWrite.getScore()); //리뷰가 추가될 때 Item의 상태를 바꿔주는 메서드
        reviews.add(review);
        return review;
    }

    public Review updateReview(User user, ReviewWrite reviewWrite, Long reviewId) {
        //리뷰를 찾아온다
        Review review = getReview(reviewId);
        review.checkMine(user);

        //찾아온 리뷰를 업데이트 한다
        reviewUpdated(review.getScore(), reviewWrite.getScore());
        return review.update(reviewWrite.getContent(), reviewWrite.getScore());
    }

    public void deleteReview(User user, Long reviewId) {
        Review review = getReview(reviewId);
        review.checkMine(user);
        reviewDeleted(review.getScore());

        reviews.remove(review);
    }

    /**
     * Item 객체안의 특정 유저의 Review를 찾아오는 메서드이다
     * 하지만 예를들어 리뷰가 많이 달려서 1000개가 넘어가는 상황을 생각해보면
     * N+1 문제가 발생할 수 있는 이 메서드에서 반복문을 돌려서 1개의 특정 리뷰를 찾는것이란 너무 비효율적이다
     * 이 메서드는 사용하지는 않고 구현만 해놓을 예정이다
     * 특정 리뷰를 찾을 때는 repository를 사용하여 찾아오도록 할것이다
     *
     * 2024.10.22 변경
     * 해당 메서드가 N+1문제를 초래할 수 있음을 확인하였다
     * 테스트코드 작성 결과 페치 조인등을 사용하여 즉시로딩되도록 값을 가져온다면 해결할 수 있었다
     *
     * 하지만 굳이 User까지 fetch Join을 하여 Review를 찾아와야 하나 싶었다
     * 그냥 reviewId로 찾아오면 되고 찾아온 리뷰가 작성한 사람과 같은지 쿼리 1번만 더 날려서 확인하면 됐었다
     * getReview() 메서드 확인
     */
    @Deprecated
    private Review getReviewWithUser(User user) {
        for (Review review : reviews) {
            if (review.getUser().equals(user)) {
                return review;
            }
        }
        throw new IllegalStateException();
    }

    private Review getReview(Long reviewId) {
        for (Review review : reviews) {
            if (review.getId().equals(reviewId)) {
                return review;
            }
        }
        throw new ObjectNotFoundException("리뷰가 없습니다");
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
