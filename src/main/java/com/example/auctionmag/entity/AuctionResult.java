package com.example.auctionmag.entity;

import java.math.BigDecimal;
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
 * @since 2022-05-26
 */
@Data
  @EqualsAndHashCode(callSuper = false)
    public class AuctionResult implements Serializable {

    private static final long serialVersionUID=1L;

      /**
     * 商品ID
     */
        @TableId("auctionId")
      private Integer auctionId;

      /**
     * 商品名称
     */
      @TableField("auctionName")
    private String auctionName;

    @TableField("auctionStartTime")
    private LocalDateTime auctionStartTime;

    @TableField("auctionEndTime")
    private LocalDateTime auctionEndTime;

      /**
     * 起拍价
     */
      @TableField("auctionStartPrice")
    private BigDecimal auctionStartPrice;

      /**
     * 成交价
     */
      @TableField("auctionDealPrice")
    private BigDecimal auctionDealPrice;

      /**
     * 买家
     */
      private String buyer;

}
