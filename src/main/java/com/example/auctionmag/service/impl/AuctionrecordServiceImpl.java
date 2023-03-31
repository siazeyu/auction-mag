package com.example.auctionmag.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.auctionmag.entity.Auction;
import com.example.auctionmag.entity.Auctionrecord;
import com.example.auctionmag.mapper.AuctionrecordMapper;
import com.example.auctionmag.service.AuctionService;
import com.example.auctionmag.service.AuctionrecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lishilin
 * @since 2022-05-22
 */
@Service
public class AuctionrecordServiceImpl extends ServiceImpl<AuctionrecordMapper, Auctionrecord> implements AuctionrecordService {

    @Autowired
    AuctionService auctionService;
    @Autowired
    AuctionrecordService auctionrecordService;

    @Override
    public List<Auctionrecord> getAuctionRecord() {
        //1找出竞拍中的所有商品记录
        List<Auctionrecord> auctionrecords = this.list();
        List<Auctionrecord> auctionrecords1 = auctionrecords.stream().map(item ->{
            Auction auction = auctionService.getById(item.getAuctionId());
            if (auction.getAuctionStatus() == 1){
                return item;
            }
            else {
                return null;
            }
        }).collect(Collectors.toList());
        return auctionrecords1;
    }
}
