package com.example.auctionmag;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableAsync  //异步任务支持
@EnableScheduling //开始定时任务支持
@SpringBootApplication
@MapperScan("com.example.auctionmag.mapper")
public class AuctionMagApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuctionMagApplication.class, args);
    }

}
