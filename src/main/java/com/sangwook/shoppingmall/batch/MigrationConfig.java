package com.sangwook.shoppingmall.batch;

import com.sangwook.shoppingmall.domain.item.Item;
import com.sangwook.shoppingmall.domain.recommend.Recommend;
import com.sangwook.shoppingmall.repository.ItemRepository;
import com.sangwook.shoppingmall.repository.RecommendRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.batch.item.data.builder.RepositoryItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Collections;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class MigrationConfig {

    private final String JOB_NAME = "migrationJob";
    private final String STEP_NAME = "migrationStep";

    private final ItemRepository itemRepository;
    private final RecommendRepository recommendRepository;

    @Bean
    public Job migrationJob(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new JobBuilder(JOB_NAME, jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(migrationStep(jobRepository, transactionManager))
                .build();

    }

    @Bean
    @JobScope
    public Step migrationStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder(STEP_NAME, jobRepository).<Item, Recommend>chunk(5, transactionManager)
                .reader(orderReader())
                .processor(orderProcessor())
                .writer(orderWriter())
                .build();
    }

    @Bean
    @StepScope
    public RepositoryItemReader<Item> orderReader() {
        return new RepositoryItemReaderBuilder<Item>()
                .name("orderReader")
                .repository(itemRepository)
                .methodName("findAll")
                .pageSize(5)
                .arguments(List.of())
                .sorts(Collections.singletonMap("id", Sort.Direction.ASC))
                .build();
    }

    @Bean
    @StepScope
    public ItemProcessor<Item, Recommend> orderProcessor() {
        return new ItemProcessor<Item, Recommend>() {
            @Override
            public Recommend process(Item item) throws Exception {
                return new Recommend(item);
            }
        };
    }

    @Bean
    @StepScope
    public RepositoryItemWriter<Recommend> orderWriter() {
        return new RepositoryItemWriterBuilder<Recommend>()
                .repository(recommendRepository)
                .methodName("save")
                .build();
    }
}
