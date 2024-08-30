package com.sangwook.shoppingmall.batch;

import com.sangwook.shoppingmall.domain.item.Item;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class ItemReader {

//    @PersistenceContext
//    EntityManagerFactory emf;
//
//    @Bean
//    public JpaPagingItemReader<Item> jpaPagingReader() {
//        return new JpaPagingItemReaderBuilder<Item>()
//                .name("itemReader")
//                .entityManagerFactory(emf)
//                .queryString("select i.id, i.category, i.name, i.price, i.reviewCount, i.reviewAverage, i.sales, i.score from Item i")
//                .pageSize(10)
//                .build();
//    }

}
