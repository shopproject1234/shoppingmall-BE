package com.sangwook.shoppingmall.presentation;

import com.sangwook.shoppingmall.common.argumentResolver.Login;
import com.sangwook.shoppingmall.entity.itemaggregate.domain.Review;
import com.sangwook.shoppingmall.entity.itemaggregate.review.dto.ReviewList;
import com.sangwook.shoppingmall.entity.itemaggregate.review.dto.ReviewWrite;
import com.sangwook.shoppingmall.entity.useraggregate.domain.User;
import com.sangwook.shoppingmall.application.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/review/{item_id}/write")
    public void write(@PathVariable("item_id") Long itemId, @Login User user, @RequestBody ReviewWrite reviewWrite) {
        Review review = reviewService.reviewWrite(user.getId(), itemId, reviewWrite);
    }

    @PatchMapping("/review/{item_id}/{review_id}/update")
    public void update(@PathVariable("item_id") Long itemId, @PathVariable("review_id") Long reviewId, @Login User user, @RequestBody ReviewWrite reviewWrite) {
        reviewService.updateReview(user, reviewId, itemId, reviewWrite);
    }

    @DeleteMapping("/review/{item_id}/{review_id}/delete")
    public void delete(@PathVariable("item_id") Long itemId, @PathVariable("review_id") Long reviewId, @Login User user) {
        reviewService.deleteReviewFetch(user, itemId, reviewId);
    }

    @GetMapping("/review/{item_id}")
    public ResponseEntity<Page<ReviewList>> list(@PathVariable("item_id") Long itemId, @RequestParam(defaultValue = "1", required = false) int page) {
        Page<ReviewList> list = reviewService.getList(itemId, page);
        return ResponseEntity.ok(list);
    }
}
