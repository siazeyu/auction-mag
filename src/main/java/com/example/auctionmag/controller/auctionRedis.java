package com.example.auctionmag.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.auctionmag.entity.Auction;
import com.example.auctionmag.service.AuctionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 项目启动时执行一次该类中方法，添加redis缓存
 */
@Component
@Order(value = 1)
public class auctionRedis implements ApplicationRunner {
    @Autowired
    AuctionService auctionService;

    @Autowired
    RedisTemplate redisTemplate;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        //1、查询未开始竞拍的商品
        QueryWrapper<Auction> wrapper1 = new QueryWrapper<>();
        wrapper1.eq("auctionStatus",2);
        List<Auction> notStartAuctions = auctionService.list(wrapper1);
        //添加到缓存中
        ValueOperations<String,List<Auction>> ops = redisTemplate.opsForValue();
        ops.set("notStartAuction",notStartAuctions);
        //2、查询竞拍中的商品
        QueryWrapper<Auction> wrapper2 = new QueryWrapper<>();
        wrapper2.eq("auctionStatus",1);
        List<Auction> auctions = auctionService.list(wrapper2);
        ops.set("auctioning",auctions);
     }
}
