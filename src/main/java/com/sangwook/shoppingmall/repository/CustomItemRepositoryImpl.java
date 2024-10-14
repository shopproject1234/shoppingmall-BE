package com.sangwook.shoppingmall.repository;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sangwook.shoppingmall.constant.Category;
import com.sangwook.shoppingmall.entity.itemaggregate.item.domain.dto.ItemList;
import com.sangwook.shoppingmall.entity.itemaggregate.item.domain.dto.QItemList;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.sangwook.shoppingmall.entity.itemaggregate.item.domain.QItemImage.itemImage;
import static com.sangwook.shoppingmall.entity.itemaggregate.item.domain.QItem.item;
import static com.sangwook.shoppingmall.entity.useraggregate.user.domain.QUser.user;


@RequiredArgsConstructor
@Transactional
@Repository
public class CustomItemRepositoryImpl implements CustomItemRepository{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<ItemList> findAllBySortType(String sortType, String keyword, String category, Pageable pageable) {
        List<ItemList> result = jpaQueryFactory.select(new QItemList(item.id, item.name, item.price, item.category, item.itemCount, item.time, user.id, user.name, itemImage.imageLink))
                .from(item)
                .innerJoin(user).on(item.user.eq(user))
                .innerJoin(itemImage).on(item.id.eq(itemImage.item.id).and(itemImage.imageNumber.eq(1)))
                .where(categoryEq(category))
                .where(keywordEq(keyword))
                .orderBy(sortTypeEq(sortType))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = jpaQueryFactory
                .select(item.count())
                .from(item)
                .where(keywordEq(keyword))
                .where(categoryEq(category))
                .fetchOne();

        total = (total != null) ? total : 0L;

        return new PageImpl<>(result, pageable, total);
    }

    private BooleanExpression keywordEq(String keyword) {
        if (keyword != null) {
            return item.name.contains(keyword);
        }
        return null;
    }

    private BooleanExpression categoryEq(String category) {
        if (category != null) {
            return item.category.eq(Category.valueOf(category.toUpperCase()));
        }
        return null;
    }

    private OrderSpecifier<?> sortTypeEq(String sortType) {

        OrderSpecifier<?> orderSpecifier =
                switch (sortType) {
                    case "latest" -> item.time.desc();
                    case "reviewCount" -> item.reviewCount.desc();
                    case "reviewRate" -> item.reviewAverage.desc();
                    default -> item.time.desc();
                };

        return orderSpecifier;
    }
}
