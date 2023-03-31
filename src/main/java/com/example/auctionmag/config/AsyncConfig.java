package com.example.auctionmag.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import java.util.concurrent.Executor;

/**
 * 注解式配置文件
 */
@Configuration  //表明该类是一个配置类
@EnableAsync    //开启异步事件的支持
@PropertySource(value = "classpath:application.properties")
public class AsyncConfig {

    /**
     * 线程池维护线程的最少数量
     */
    @Value("${corePoolSize}")
    private int corePoolSize;

    /**
     * 线程池维护线程的最大数量
     */
    @Value("${corePoolSize}")
    private int maxPoolSize;

    /**
     * 缓存队列
     */
    @Value("${corePoolSize}")
    private int queueCapacity;

    @Bean
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.initialize();
        return executor;
    }
}
