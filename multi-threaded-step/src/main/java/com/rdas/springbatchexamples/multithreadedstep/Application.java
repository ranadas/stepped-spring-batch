package com.rdas.springbatchexamples.multithreadedstep;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
//https://www.captechconsulting.com/blogs/spring-batch-multi-threaded-step
//http://www.baeldung.com/introduction-to-spring-batch
//https://github.com/tarrantrj/spring-batch-examples/tree/master/multi-threaded-step
@SpringBootApplication
@EnableBatchProcessing
public class Application extends DefaultBatchConfigurer {

    public final static Logger logger = LoggerFactory.getLogger(Application.class);

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Value("${chunk-size}")
    private int chunkSize;

    @Value("${max-threads}")
    private int maxThreads;

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource batchDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Autowired
    private JobCompletionNotificationListener jobCompletionNotificationListener;

    @Autowired
    private Step step1;

    @Bean
    public TaskExecutor taskExecutor() {
        SimpleAsyncTaskExecutor taskExecutor = new SimpleAsyncTaskExecutor();
        taskExecutor.setConcurrencyLimit(maxThreads);
        return taskExecutor;
    }

    @Bean
    public Job processAttemptJob() {
        return jobBuilderFactory.get("process-attempt-job")
                .incrementer(new RunIdIncrementer())
                .listener(jobCompletionNotificationListener)
                .flow(step1)
                .end()
                .build();
    }


    public static void main(String[] args) {
        long time = System.currentTimeMillis();
        SpringApplication.run(Application.class, args);
        time = System.currentTimeMillis() - time;
        logger.info("Runtime: {} seconds.", ((double) time / 1000));
    }
}
