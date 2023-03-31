package com.example.auctionmag.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class auctionResultVo {
    String auctionName;
    String userName;//买家
    LocalDateTime auctionStartTime;
    LocalDateTime auctionEndTime;
    BigDecimal dealAuctionPrice;//交易价格
}
