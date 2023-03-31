package com.example.auctionmag.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 *
 * </p>
 *
 * @author lishilin
 * @since 2022-05-22
 */
@Data
  @EqualsAndHashCode(callSuper = false)
    public class Auction implements Serializable {

    private static final long serialVersionUID=1L;

      @TableId(value = "auctionId", type = IdType.AUTO)
      private Integer auctionId;

    @TableField("auctionName")
    private String auctionName;
    //竞拍状态
  @TableField("auctionStatus")
    private Integer auctionStatus;

    @TableField("auctionStartPrice")
    private BigDecimal auctionStartPrice;

    @TableField("auctionUpset")
    private BigDecimal auctionUpset;

    @TableField("auctionStartTime")
    private LocalDateTime auctionStartTime;

    @TableField("auctionEndTime")
    private LocalDateTime auctionEndTime;

    @TableField("auctionPic")
    private String auctionPic;

    @TableField("auctionPicType")
    private String auctionPicType;

    @TableField("auctionDesc")
    private String auctionDesc;
    @TableField("highestPrice")
    private BigDecimal highestPrice;//最高价

}
