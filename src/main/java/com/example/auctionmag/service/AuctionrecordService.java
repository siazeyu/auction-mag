package com.example.auctionmag.service;

import com.example.auctionmag.entity.Auctionrecord;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lishilin
 * @since 2022-05-22
 */
public interface AuctionrecordService extends IService<Auctionrecord> {

    List<Auctionrecord> getAuctionRecord();
}
