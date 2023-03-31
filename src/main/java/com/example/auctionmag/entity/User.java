package com.example.auctionmag.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
    public class User implements Serializable {

    private static final long serialVersionUID=1L;

      /**
     * 用户表
     */
        @TableId(value = "userId", type = IdType.AUTO)
      private Integer userId;

    @TableField("userName")
    private String userName;

    @TableField("userPassword")
    private String userPassword;

    @TableField("userCardNo")
    private String userCardNo;

    @TableField("userTel")
    private String userTel;

    @TableField("userAddress")
    private String userAddress;

    @TableField("userPostNumber")
    private String userPostNumber;

    @TableField("userIsadmin")
    private Integer userIsadmin;

    @TableField("userQuestion")
    private String userQuestion;

    @TableField("userAnswer")
    private String userAnswer;


}
