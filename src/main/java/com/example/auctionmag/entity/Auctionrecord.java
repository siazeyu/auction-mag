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
    public class Auctionrecord implements Serializable {

    private static final long serialVersionUID=1L;

      @TableId(value = "id", type = IdType.AUTO)
      private Integer id;

    @TableField("userId")
    private Integer userId;

    @TableField("auctionId")
    private Integer auctionId;

    @TableField("auctionTime")
    private LocalDateTime auctionTime;

    @TableField("auctionPrice")
    private BigDecimal auctionPrice;


}
