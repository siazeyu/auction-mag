package com.example.auctionmag.service.impl;

import com.example.auctionmag.entity.Auction;
import com.example.auctionmag.entity.AuctionResult;
import com.example.auctionmag.entity.Auctionrecord;
import com.example.auctionmag.entity.User;
import com.example.auctionmag.mapper.AuctionResultMapper;
import com.example.auctionmag.service.AuctionResultService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.auctionmag.service.AuctionService;
import com.example.auctionmag.service.UserService;
import com.example.auctionmag.vo.AuctionRecordUserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lishilin
 * @since 2022-05-26
 */
@Service
public class AuctionResultServiceImpl extends ServiceImpl<AuctionResultMapper, AuctionResult> implements AuctionResultService {

    @Autowired
    UserService userService;
    @Autowired
    AuctionService auctionService;

    @Override
    public List<AuctionRecordUserVo> getUserVo(List<Auctionrecord> records) {
        System.out.println("竞拍记录："+records);
            //获取每一个的所有记录，并放进集合中
            List<AuctionRecordUserVo> userVos = records.stream().map(record -> {
                if(record != null) {
                    User user = userService.getById(record.getUserId());
                    Auction auction = auctionService.getById(record.getAuctionId());
                    AuctionRecordUserVo vo = new AuctionRecordUserVo();
                    String userName = user.getUserName();
                    String auctionName = auction.getAuctionName();
                    vo.setAuctionName(auctionName);
                    vo.setAuctionStartTime(auction.getAuctionStartTime());
                    vo.setAuctionTime(record.getAuctionTime());
                    vo.setAuctionEndTime(auction.getAuctionEndTime());
                    vo.setAuctionStartPrice(auction.getAuctionStartPrice());
                    vo.setAuctionPrice(record.getAuctionPrice());
                    vo.setUserName(userName);
                    return vo;
                }
                else {
                    return null;
                }
            }).collect(Collectors.toList());
            //将该集合放进一个大的list中
        return userVos;
    }
}
