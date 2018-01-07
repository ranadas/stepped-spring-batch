package com.rdas.springbatchexamples.multithreadedstep;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;

/**
 * Created by rdas on 06/01/2018.
 */
@ComponentScan(basePackages = "com.rdas")
@Component
@Configuration
public class StepConfig {

    @Autowired
    public StepBuilderFactory stepBuilderFactory;
    @Value("${chunk-size}")
    private int chunkSize;

    @Value("${max-threads}")
    private int maxThreads;

    @Bean
    public Step step1(@Autowired AttemptReader attemptReader,
                     @Autowired AttemptProcessor attemptProcessor,
                     @Autowired AttemptWriter attemptWriter,
                     @Autowired TaskExecutor taskExecutor,
                     @Autowired StepExecutionNotificationListener stepExecutionNotificationListener,
                     @Autowired ChunkExecutionListener chunkExecutionListener) {
        return stepBuilderFactory.get("step")
                .<Attempt, Attempt>chunk(chunkSize)
                .reader(attemptReader)
                .processor(attemptProcessor)
                .writer(attemptWriter)
                .taskExecutor(taskExecutor)
                .listener(stepExecutionNotificationListener)
                .listener(chunkExecutionListener)
                .throttleLimit(maxThreads).build();
    }
}
