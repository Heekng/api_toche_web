package com.heekng.api_toche_web.batch.job.match;

import com.heekng.api_toche_web.batch.chunk.processor.ApiMatchDetailProcessor;
import com.heekng.api_toche_web.batch.chunk.writer.JpaItemListWriter;
import com.heekng.api_toche_web.entity.MatchInfo;
import com.heekng.api_toche_web.entity.TftMatch;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;
import java.util.List;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class ApiMatchDetailJobConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory emf;
    private final ApiMatchDetailProcessor apiMatchDetailProcessor;

    @Bean
    public Job apiMatchDetailJob() {
        return jobBuilderFactory.get("apiMatchDetailJob")
                .start(apiMatchDetailStep())
                .build();
    }

    public Step apiMatchDetailStep() {
        return stepBuilderFactory.get("apiMatchDetailStep")
                .<TftMatch, List<MatchInfo>>chunk(10)
                .reader(apiMatchDetailReader())
                .processor(apiMatchDetailProcessor)
                .writer(apiMatchDetailWriter())
                .build();
    }

    private ItemWriter<List<MatchInfo>> apiMatchDetailWriter() {
        JpaItemWriter<MatchInfo> writer = new JpaItemWriter<>();
        writer.setEntityManagerFactory(emf);

        return new JpaItemListWriter<>(writer);
    }

    private ItemReader<TftMatch> apiMatchDetailReader() {
        return new JpaPagingItemReaderBuilder<TftMatch>()
                .name("apiMatchDetailReader")
                .entityManagerFactory(emf)
                .pageSize(10)
                .queryString("select tm from TftMatch tm " +
                        "left join MatchInfo mi on tm = mi.tftMatch " +
                        "where mi.gameDatetime IS NULL " +
                        "and (tm.gameType = 'standard' or tm.gameType = null)")
                .build();
    }


}
