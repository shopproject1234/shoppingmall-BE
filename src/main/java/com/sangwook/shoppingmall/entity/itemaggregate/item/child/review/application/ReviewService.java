package com.sangwook.shoppingmall.entity.itemaggregate.item.child.review.application;

import com.sangwook.shoppingmall.entity.historyaggregate.history.domain.History;
import com.sangwook.shoppingmall.entity.itemaggregate.item.child.review.domain.dto.ReviewList;
import com.sangwook.shoppingmall.entity.itemaggregate.item.domain.Item;
import com.sangwook.shoppingmall.entity.useraggregate.user.domain.User;
import com.sangwook.shoppingmall.entity.itemaggregate.item.domain.Review;
import com.sangwook.shoppingmall.entity.itemaggregate.item.child.review.domain.dto.ReviewWrite;
import com.sangwook.shoppingmall.exception.custom.ObjectAlreadyExistException;
import com.sangwook.shoppingmall.exception.custom.ObjectNotFoundException;
import com.sangwook.shoppingmall.entity.historyaggregate.history.infra.HistoryRepository;
import com.sangwook.shoppingmall.entity.itemaggregate.item.infra.ItemRepository;
import com.sangwook.shoppingmall.entity.useraggregate.user.infra.UserRepository;
import com.sangwook.shoppingmall.entity.itemaggregate.item.child.review.infra.ReviewRepository;
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

    public Review reviewWrite(Long userId, Long itemId, ReviewWrite reviewWrite) {
        // 유저와 아이템을 바탕으로 구매 이력 조회
        isPurchased(userId, itemId);

        // 이미 작성한 리뷰가 있을 경우 예외 발생
        // 조회만을 위한 repository 사용은 ddd의 개념에 위배되지 않는다 (Item 객체 내부의 List<Review>에서 탐색하는 것보다 효율적)
        Optional<Review> review = reviewRepository.findByItemIdAndUserId(itemId, userId);
        if (review.isPresent()) {
            throw new ObjectAlreadyExistException("리뷰가 이미 존재합니다", getMethodName());
        }

        User user = getUser(userId);
        Item item = getItem(itemId);
        return item.writeReview(user, reviewWrite);
    }

    public Review updateReview(User user, Long reviewId, Long itemId, ReviewWrite reviewWrite) {
        Item item = getItemFetchReview(itemId);
        return item.updateReview(user, reviewWrite, reviewId);
    }

    /**
     * N+1이 발생하는 코드, 테스트 코드에서만 사용
     */
    @Deprecated
    public void deleteReview(User user, Long reviewId, Long itemId) {
        Item item = getItem(itemId);
        item.deleteReview(user, reviewId);
    }

    /**
     * N+1을 해결한 코드
     */
    public void deleteReviewFetch(User user, Long reviewId, Long itemId) {
        Item item = getItemFetchReview(itemId);
        item.deleteReview(user, reviewId);
    }


    public Page<ReviewList> findReview(Long itemId, int page) {
        PageRequest pageRequest = PageRequest.of(page - 1, 10);
        return reviewRepository.getList(itemId, pageRequest);
    }

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

    private Item getItemFetchReview(Long itemId) {
        Optional<Item> item = itemRepository.findItemFetchReview(itemId);
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
}
