package com.example.auctionmag.service;

import com.example.auctionmag.entity.AuctionResult;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.auctionmag.entity.Auctionrecord;
import com.example.auctionmag.vo.AuctionRecordUserVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lishilin
 * @since 2022-05-26
 */
public interface AuctionResultService extends IService<AuctionResult> {
    List<AuctionRecordUserVo> getUserVo(List<Auctionrecord> auctionrecords);
}
