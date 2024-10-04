package com.sangwook.shoppingmall.controller;

import com.sangwook.shoppingmall.argumentResolver.Login;
import com.sangwook.shoppingmall.domain.review.Review;
import com.sangwook.shoppingmall.domain.review.dto.ReviewList;
import com.sangwook.shoppingmall.domain.review.dto.ReviewWrite;
import com.sangwook.shoppingmall.domain.user.User;
import com.sangwook.shoppingmall.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/review/{item_id}/write")
    public void write(@PathVariable("item_id") Long itemId, @Login User user, @RequestBody ReviewWrite reviewWrite) {
        Review review = reviewService.reviewWrite(user.getId(), itemId, reviewWrite);
    }

    @PatchMapping("/review/{review_id}/update")
    public void update(@PathVariable("review_id") Long reviewId, @Login User user, @RequestBody ReviewWrite reviewWrite) {
        reviewService.updateReview(user, reviewId, reviewWrite);
    }

    @DeleteMapping("/review/{review_id}/delete")
    public void delete(@PathVariable("review_id") Long reviewId, @Login User user) {
        reviewService.deleteReview(user, reviewId);
    }

    @GetMapping("/review/{item_id}")
    public Page<ReviewList> getReview(@PathVariable("item_id") Long itemId, @RequestParam int page) {
        return reviewService.findReview(itemId, page);
    }
}
