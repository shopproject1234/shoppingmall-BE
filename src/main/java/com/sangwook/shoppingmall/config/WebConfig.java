package com.sangwook.shoppingmall.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sangwook.shoppingmall.argumentResolver.LoginArgumentResolver;
import com.sangwook.shoppingmall.argumentResolver.ScaleArgumentResolver;
import com.sangwook.shoppingmall.interceptor.LoginInterceptor;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.addAll(List.of(new LoginArgumentResolver(), new ScaleArgumentResolver()));
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) { //FIXME 배포 시 수정 필요
        registry.addInterceptor(new LoginInterceptor())
                .order(1)
                .excludePathPatterns("/**");
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    //QueryDsl
    @PersistenceContext
    private EntityManager entityManager;

    @Bean
    public JPAQueryFactory jpaQueryFactory() {
        return new JPAQueryFactory(entityManager);
    }
}
