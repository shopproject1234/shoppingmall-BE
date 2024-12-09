package com.sangwook.shoppingmall.config;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncConfig implements AsyncConfigurer {

    /**
     * ExecutorService의 설정
     * setCorePoolSize - 기본으로 유지되는 쓰레드 개수
     * setQueueCapacity - 기본으로 유지되는 쓰레드가 작업중일 때 다음 task를 저장할 queue의 사이즈
     * setMaxPoolSize - 기본으로 유지되는 쓰레드도 모두 작업중이고, Queue에 task도 모두 찼을 때 늘어나게 되는 쓰레드의 최대 개수
     */

    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2); //동시에 실행시킬 쓰레드의 개수
        executor.setMaxPoolSize(5); //쓰레드풀의 최대 사이즈
        executor.setQueueCapacity(10); //corePoolSize에 넘어서는 task가 들어왔을때 Queue에 task를 쌓는다, 여기서는 최대 10까지 쌓임
        executor.setThreadNamePrefix("Async MailExecutor-"); //쓰레드 이름 지정
        executor.initialize();
        return executor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return AsyncConfigurer.super.getAsyncUncaughtExceptionHandler();
    }
}
