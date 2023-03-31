package com.example.auctionmag.mapper;

import com.example.auctionmag.entity.Auction;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.math.BigDecimal;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author lishilin
 * @since 2022-05-22
 */
public interface AuctionMapper extends BaseMapper<Auction> {

    @Select("select max(auctionPrice) from auctionrecord where auctionid=#{auctionId}")
    BigDecimal getMaxPrice(@Param("auctionId") Integer auctionId);

    @Update("UPDATE auction SET auctionStatus = 1 WHERE auctionId=#{auctionId}")
    void setAuctioning(@Param("auctionId") Integer auctionId);

    @Update("update auction set auctionStatus = 0 where auctionId=#{auctionId}")
    void updataAuctionStatus(@Param("auctionId") Integer auctionId);
}
