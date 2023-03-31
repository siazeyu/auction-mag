package com.example.auctionmag.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.auctionmag.entity.Auction;
import com.example.auctionmag.entity.AuctionResult;
import com.example.auctionmag.entity.Auctionrecord;
import com.example.auctionmag.entity.User;
import com.example.auctionmag.mapper.AuctionMapper;
import com.example.auctionmag.mapper.AuctionrecordMapper;
import com.example.auctionmag.service.AuctionResultService;
import com.example.auctionmag.service.AuctionService;
import com.example.auctionmag.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class AutoDealAuctionTask {
    @Autowired
    private AuctionService auctionService;
    @Autowired
    private AuctionMapper auctionMapper;
    @Autowired
    private AuctionrecordMapper auctionrecordMapper;
    @Autowired
    private AuctionResultService auctionResultService;
    @Autowired
    UserService userService;
    @Autowired
    RedisTemplate redisTemplate;


    /**
     * 处理开始竞拍
     */
    @Async
    //corn从左到右（用空格隔开）：秒 分 小时 月份中的日期 月份 星期中的日期 年份
    @Scheduled(cron = "* * * * * *")   //每秒执行一次
    public void startAuctionTask() {
        this.startAuction();
    }
    @Async
    @Scheduled(cron = "* * * * * *")//每秒执行一次
    public void handleEndAuction(){
        this.endAuction();
    }
    /**
     * 监控商品是否开始竞拍
     */
    public void startAuction(){
        //从缓存中拿出还未开始竞拍的商品
        ValueOperations<String, List<Auction>> ops = redisTemplate.opsForValue();
        List<Auction> auctions = ops.get("notStartAuction");
        if (auctions != null) {
            for (Auction auction : auctions) {
                if(auction.getAuctionStatus()==2) {
                    LocalDateTime nowTime = LocalDateTime.now();
                    LocalDateTime beginTime = auction.getAuctionStartTime();
                    LocalDateTime endTime = auction.getAuctionEndTime();
                    boolean flag = inAuctionTime(nowTime, beginTime, endTime);
                    //如果达到竞拍开始时间，则改变竞拍状态为竞拍中，auctionStatus = 1；
                    if (flag) {
                        this.auctionMapper.setAuctioning(auction.getAuctionId());
                        System.out.println(auction.getAuctionName() + "开始竞拍");
                        //竞拍中商品增加，添加竞拍中的缓存
                        QueryWrapper<Auction> wrapper = new QueryWrapper<>();
                        wrapper.eq("auctionStatus", 1);
                        List<Auction> auctions1 = auctionService.list(wrapper);
                        //将竞拍中的商品添加到缓存中
                        redisTemplate.delete("auctioning");
                        ops.set("auctioning", auctions1);
                        //更新未开始竞拍的缓存
                        QueryWrapper<Auction> wrapper1 = new QueryWrapper<>();
                        wrapper1.eq("auctionStatus",2);
                        List<Auction> notStart= this.auctionService.list(wrapper1);
                        redisTemplate.delete("notStartAuction");
                        ops.set("notStartAuction",notStart);
                    }
                }
            }
        }
    }

    /**
     * 处理竞拍结果
     */
    public void endAuction(){
        ValueOperations<String, List<Auction>> ops = redisTemplate.opsForValue();
        List<Auction> auctions = ops.get("auctioning");
        if (auctions != null){
            for (Auction auction:auctions){
                if(auction.getAuctionStatus()==1) {
                    LocalDateTime nowTime = LocalDateTime.now();
                    LocalDateTime endTime = auction.getAuctionEndTime();
                    //System.out.println(endTime);
                    boolean flag = this.endAuctionTime(nowTime, endTime);
                    //到达竞拍时间，处理竞拍结果
                    if (flag) {
                        //改变竞拍状态
                        this.auctionMapper.updataAuctionStatus(auction.getAuctionId());
                        System.out.println("成功更新竞拍状态");
                        AuctionResult auctionResult = new AuctionResult();
                        String buyer = this.handleAuctionResult(auction.getAuctionId(), auction.getHighestPrice());
                        auctionResult.setAuctionId(auction.getAuctionId());
                        auctionResult.setAuctionName(auction.getAuctionName());
                        auctionResult.setAuctionStartTime(auction.getAuctionStartTime());
                        auctionResult.setAuctionEndTime(auction.getAuctionEndTime());
                        auctionResult.setAuctionStartPrice(auction.getAuctionStartPrice());
                        auctionResult.setAuctionDealPrice(auction.getHighestPrice());
                        auctionResult.setBuyer(buyer);
                        //保存竞拍结果
                        if(auctionResult != null) {
                            auctionResultService.save(auctionResult);
                        }
                        //更新缓存
                        QueryWrapper<Auction> wrapper = new QueryWrapper<Auction>().eq("auctionStatus", 1);
                        List<Auction> auctions1 = this.auctionService.list(wrapper);
                        redisTemplate.delete("auctioning");
                        ops.set("auctioning", auctions1);
                        System.out.println("成功保存竞拍结果:" + auctionResult);
                    }
                }
            }
        }
    }
 //   @Async
//    @Scheduled(cron = "* * * * * *")
//    public void test(){
//        LocalDateTime nowTime = LocalDateTime.now();
//        Auction auction = auctionService.getById(46);
//        System.out.println(auction.getAuctionEndTime());
        //System.out.println(nowTime);
//        LocalDateTime endTime = LocalDateTime.parse("2022-05-27 16:34:00");
//        System.out.println(endTime);
//        if (endAuctionTime(nowTime,endTime)){
//            System.out.println("结束时间到了");
//        }
   // }
    //获取买家用户名
    public String handleAuctionResult(Integer auctionId, BigDecimal highestPrice){
        //获取出价最高的竞拍记录
        Auctionrecord auctionrecord = this.auctionrecordMapper.getRecord(auctionId,highestPrice);
        //获取买家用户名
        if(auctionrecord != null){
            User user = userService.getById(auctionrecord.getUserId());
            return user.getUserName();
        }
        else{
            return null;
        }
    }

    //用来判断该商品是否在竞拍时间内
    public boolean inAuctionTime(LocalDateTime time, LocalDateTime beginTime, LocalDateTime endTime) {
        return (time.isAfter(beginTime) && time.isBefore(endTime));
    }
    //结束竞拍时间
    public boolean endAuctionTime(LocalDateTime time,LocalDateTime endTime) {
        return time.isAfter(endTime);
    }

}
