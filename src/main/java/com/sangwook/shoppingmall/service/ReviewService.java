package com.sangwook.shoppingmall.service;

import com.sangwook.shoppingmall.domain.history.History;
import com.sangwook.shoppingmall.domain.item.Item;
import com.sangwook.shoppingmall.domain.user.User;
import com.sangwook.shoppingmall.domain.review.Review;
import com.sangwook.shoppingmall.domain.review.dto.ReviewWrite;
import com.sangwook.shoppingmall.repository.HistoryRepository;
import com.sangwook.shoppingmall.repository.ItemRepository;
import com.sangwook.shoppingmall.repository.UserRepository;
import com.sangwook.shoppingmall.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final HistoryRepository historyRepository;

    public Review reviewWrite(Long userId, Long itemId, ReviewWrite reviewWrite) {
        // 유저와 아이템을 바탕으로 구매 이력 조회
        if (!isPurchased(userId, itemId)) { // 구매하지 않은 상품이라면 예외처리
            throw new IllegalStateException(); //FIXME
        }

        User user = getUser(userId);
        Item item = getItem(itemId);

        Review review = Review.write(user, item, reviewWrite);
        item.plusReview(review);
        return reviewRepository.save(review);
    }

    public Review updateReview(User user, Long reviewId, ReviewWrite reviewWrite) {
        Review review = getReview(reviewId);
        if (!user.equals(review.getUser())) { // 요청한 유저와 리뷰를 작성한 유저가 다른 경우
            throw new IllegalStateException(); //FIXME
        }
        return review.update(reviewWrite);
    }

    public void deleteReview(User user, Long reviewId) {
        Review review = getReview(reviewId);
        if (!user.equals(review.getUser())) { // 요청한 유저와 리뷰를 작성한 유저가 다른 경우
            throw new IllegalStateException(); //FIXME
        }
        reviewRepository.delete(review);
    }


    public void findReview(Long itemId, Pageable pageable) {
        int page = pageable.getPageNumber() - 1;
        PageRequest pageRequest = PageRequest.of(page, 10);
    }

    /**
     * private Method
     */
    private User getUser(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new IllegalStateException(); //FIXME
        }
        return user.get();
    }

    private Item getItem(Long itemId) {
        Optional<Item> item = itemRepository.findById(itemId);
        if (item.isEmpty()) {
            throw new IllegalStateException(); //FIXME
        }
        return item.get();
    }

    private Review getReview(Long reviewId) {
        Optional<Review> review = reviewRepository.findById(reviewId);
        if (review.isEmpty()) {
            throw new IllegalStateException(); //FIXME
        }
        return review.get();
    }

    private Boolean isPurchased(Long userId, Long itemId) {
        Optional<History> history = historyRepository.findByUserIdAndItemId(userId, itemId);
        return history.isPresent();
    }
}
