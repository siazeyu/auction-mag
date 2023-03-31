package com.example.auctionmag.mapper;

import com.example.auctionmag.entity.Auctionrecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author lishilin
 * @since 2022-05-22
 */
public interface AuctionrecordMapper extends BaseMapper<Auctionrecord> {
    @Select("SELECT * FROM auctionrecord WHERE auctionId=#{auctionId} AND auctionPrice=#{highestPrice}")
    Auctionrecord getRecord(@Param("auctionId") Integer auctionId,@Param("highestPrice") BigDecimal highestPrice);
}
