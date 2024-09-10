package com.sangwook.shoppingmall.service;

import com.sangwook.shoppingmall.domain.item.Item;
import com.sangwook.shoppingmall.domain.user.User;
import com.sangwook.shoppingmall.domain.review.Review;
import com.sangwook.shoppingmall.domain.review.dto.ReviewPage;
import com.sangwook.shoppingmall.domain.review.dto.ReviewWrite;
import com.sangwook.shoppingmall.repository.ItemRepository;
import com.sangwook.shoppingmall.repository.MemberRepository;
import com.sangwook.shoppingmall.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    public void reviewWrite(User user, Long itemId, ReviewWrite reviewWrite) {
        User getUser = memberRepository.findById(user.getId()).get();
        Item getItem = itemRepository.findById(itemId).get();

        Review review = new Review(getUser, getItem, reviewWrite);
        getItem.plusReview(review);

        reviewRepository.save(review);

    }

    public void findReview(Long itemId, Pageable pageable) {
        int page = pageable.getPageNumber() - 1;
        PageRequest pageRequest = PageRequest.of(page, 10);
    }
}
