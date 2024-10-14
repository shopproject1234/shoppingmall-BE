package com.sangwook.shoppingmall.entity.itemaggregate.item.child.review.application;

import com.sangwook.shoppingmall.entity.historyaggregate.history.infra.History;
import com.sangwook.shoppingmall.entity.itemaggregate.item.domain.Item;
import com.sangwook.shoppingmall.entity.itemaggregate.item.child.review.domain.dto.ReviewList;
import com.sangwook.shoppingmall.entity.useraggregate.user.domain.User;
import com.sangwook.shoppingmall.entity.itemaggregate.item.domain.Review;
import com.sangwook.shoppingmall.entity.itemaggregate.item.child.review.domain.dto.ReviewWrite;
import com.sangwook.shoppingmall.exception.custom.ObjectNotFoundException;
import com.sangwook.shoppingmall.exception.custom.UserValidationException;
import com.sangwook.shoppingmall.repository.HistoryRepository;
import com.sangwook.shoppingmall.repository.ItemRepository;
import com.sangwook.shoppingmall.repository.UserRepository;
import com.sangwook.shoppingmall.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.sangwook.shoppingmall.exception.MethodFunction.getMethodName;

@Service
@Transactional
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final HistoryRepository historyRepository;

    //TODO 양방향 연관관계 테스트 필요
    public Review reviewWrite(Long userId, Long itemId, ReviewWrite reviewWrite) {
        // 유저와 아이템을 바탕으로 구매 이력 조회
        isPurchased(userId, itemId);

        User user = getUser(userId);
        Item item = getItem(itemId);

        Review review = item.writeReview(user, reviewWrite);
        return reviewRepository.save(review);
    }

    public Review updateReview(User user, Long reviewId, Long itemId, ReviewWrite reviewWrite) {
        Item item = getItem(itemId);
        Review review = getReview(reviewId);
        checkMine(user, review);
        return item.updateReview(review, reviewWrite);
    }


    public void deleteReview(User user, Long reviewId, Long itemId) {
        Item item = getItem(itemId);
        Review review = getReview(reviewId);
        checkMine(user, review);
        reviewRepository.delete(review);
    }


//    public Page<ReviewList> findReview(Long itemId, int page) {
//        PageRequest pageRequest = PageRequest.of(page - 1, 10);
//        return reviewRepository.getList(itemId, pageRequest);
//    }

    /**
     * private Method
     */
    private User getUser(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new ObjectNotFoundException(getMethodName());
        }
        return user.get();
    }

    private Item getItem(Long itemId) {
        Optional<Item> item = itemRepository.findById(itemId);
        if (item.isEmpty()) {
            throw new ObjectNotFoundException(getMethodName());
        }
        return item.get();
    }

    private Review getReview(Long reviewId) {
        Optional<Review> review = reviewRepository.findById(reviewId);
        if (review.isEmpty()) {
            throw new ObjectNotFoundException(getMethodName());
        }
        return review.get();
    }

    private void isPurchased(Long userId, Long itemId) {
        Optional<History> history = historyRepository.findByUserIdAndItemId(userId, itemId);
        if (history.isEmpty()) {
            throw new ObjectNotFoundException("구매 하지 않은 상품에는 리뷰를 작성할 수 없습니다", getMethodName());
        }
    }

    private void checkMine(User user, Review review) {
        if (!user.equals(review.getUser())) { // 요청한 유저와 리뷰를 작성한 유저가 다른 경우
            throw new UserValidationException(getMethodName());
        }
    }
}
