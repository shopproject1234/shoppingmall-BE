package com.sangwook.shoppingmall.concurrent;

import com.sangwook.shoppingmall.constant.Category;
import com.sangwook.shoppingmall.constant.Gender;
import com.sangwook.shoppingmall.entity.cartaggregate.cart.domain.dto.AddCart;
import com.sangwook.shoppingmall.entity.itemaggregate.item.application.ItemService;
import com.sangwook.shoppingmall.entity.itemaggregate.item.domain.Item;
import com.sangwook.shoppingmall.entity.itemaggregate.item.domain.dto.AddItem;
import com.sangwook.shoppingmall.entity.useraggregate.user.application.UserService;
import com.sangwook.shoppingmall.entity.useraggregate.user.domain.User;
import com.sangwook.shoppingmall.entity.useraggregate.user.domain.dto.UserRegister;
import com.sangwook.shoppingmall.service.fake.FakeCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.List;

@Configuration
@Profile("orderTest")
public class OrderConcurrentDataInitializer {

    @Autowired
    UserService userService;

    @Autowired
    ItemService itemService;

    @Autowired
    FakeCartService cartService;

    User user;
    Item item;

    @Bean
    public CommandLineRunner init() {
        return args -> {
            UserRegister userRegister = new UserRegister("tkddnr@naver.com",
                    "상욱", "123123",
                    "01011112222", "2012-11-22",
                    Gender.MALE, List.of());
            user = userService.register(userRegister);

            AddItem addItem = new AddItem();
            addItem.setItemName("장롱");
            addItem.setItemCount(3);
            addItem.setItemInfo("장롱입니다 매우 상태가 좋습니다");
            addItem.setCategory(Category.FURNITURE);
            addItem.setPrice(1_000_000);
            List<String> image = List.of("imageLink1", "imageLink2", "imageLink3");
            addItem.setImage(image);
            item = itemService.add(user, addItem);

            UserRegister userRegister2 = new UserRegister("taaaa141414@naver.com",
                    "상욱2", "123123",
                    "01011112222", "2012-11-22",
                    Gender.MALE, List.of());
            User customer1 = userService.register(userRegister2);

            User customer2 = userService.register(new UserRegister("tatata123123@naver.com",
                    "상욱3", "123123",
                    "01011112222", "2012-11-22",
                    Gender.MALE, List.of()));

            //상품은 3개인데 2개 2개씩 사용자가 카트에 추가하여 주문한다
            AddCart addCart = new AddCart();
            addCart.setItemId(item.getId());
            addCart.setItemCount(2);

            cartService.add(customer1.getId(), addCart);
            cartService.add(customer2.getId(), addCart);
        };
    }
}
