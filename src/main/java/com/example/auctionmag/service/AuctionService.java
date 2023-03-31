package com.example.auctionmag.service;

import com.example.auctionmag.entity.Auction;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.auctionmag.entity.Auctionrecord;
import com.example.auctionmag.vo.AuctionRecordUserVo;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lishilin
 * @since 2022-05-22
 */
public interface AuctionService extends IService<Auction> {

    List<Auction> seleteAuction();

    List<AuctionRecordUserVo> getRecordUserName(List<Auctionrecord> auctionrecords);

    List<Auction> searchAcution(Map<String, Object> map);

    boolean analyzePrice(Integer auctionId, BigDecimal auctionPrice);
}
