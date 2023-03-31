package com.example.auctionmag.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class AuctionRecordUserVo {
    private String userName;
    private String auctionName;
    private BigDecimal auctionStartPrice;
    private LocalDateTime auctionTime;
    private LocalDateTime auctionStartTime;
    private LocalDateTime auctionEndTime;
    private BigDecimal auctionPrice;
}
