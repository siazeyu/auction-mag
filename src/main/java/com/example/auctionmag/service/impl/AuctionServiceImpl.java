package com.example.auctionmag.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.auctionmag.entity.Auction;
import com.example.auctionmag.entity.Auctionrecord;
import com.example.auctionmag.entity.User;
import com.example.auctionmag.mapper.AuctionMapper;
import com.example.auctionmag.service.AuctionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.auctionmag.service.UserService;
import com.example.auctionmag.vo.AuctionRecordUserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lishilin
 * @since 2022-05-22
 */
@Service
public class AuctionServiceImpl extends ServiceImpl<AuctionMapper, Auction> implements AuctionService {
    @Autowired
    UserService userService;
    @Override
    public List<Auction> seleteAuction(String name, String des, BigDecimal price) {
        QueryWrapper<Auction> wrapper = new QueryWrapper<>();
        wrapper.eq("auctionStatus",1);
        if (name != null && !"".equals(name)){
            wrapper.and((w)->{
                w.like("auctionName",name);
            });
        }
        if (des!=null && !des.equals("")){
            wrapper.and((w)->{
                w.like("auctionDesc",des);
            });
        }
        wrapper.and((w)->{
            w.ge("auctionStartPrice",price);//大于等于竞拍价
        });
        return this.list(wrapper);
    }

    /**
     * 封装竞拍记录返回
     * @param auctionrecords
     * @return
     *
     */
    @Override
    public List<AuctionRecordUserVo> getRecordUserName(List<Auctionrecord> auctionrecords) {
        List<AuctionRecordUserVo> collect = auctionrecords.stream().map(record -> {
            AuctionRecordUserVo vo = new AuctionRecordUserVo();
            Auction auction = this.baseMapper.selectById(record.getAuctionId());
            User user = userService.getById(record.getUserId());
            vo.setAuctionName(auction.getAuctionName());
            vo.setUserName(user.getUserName());
            vo.setAuctionTime(record.getAuctionTime());
            vo.setAuctionPrice(record.getAuctionPrice());
            return vo;
        }).collect(Collectors.toList());
        return collect;
    }

    /**
     * 查询竞拍商品
     * @param map
     * @return
     */
    @Override
    public List<Auction> searchAcution(Map<String, Object> map) {
        QueryWrapper<Auction> wrapper = new QueryWrapper();
        String auctionName = (String) map.get("auctionName");
        String auctionDesc = (String) map.get("auctionDesc");
        String auctionStartPrice = (String) map.get("auctionStartPrice");
        if (auctionName!=null){
            wrapper.and((w)->{
                w.like("auctionName",auctionName);
            });
        }
        if (auctionDesc!=null){
            wrapper.and((w)->{
                w.like("auctionDesc",auctionDesc);
            });
        }
        if (auctionStartPrice!=null){
            try{
                BigDecimal price = new BigDecimal(auctionStartPrice);
                wrapper.and((w)->{
                    w.ge("auctionStartPrice",price==null ? 0:price);//大于等于竞拍价
                });
            }
            catch (Exception e){
                System.out.println(e);
            }
        }
        List<Auction> auctions = this.baseMapper.selectList(wrapper);
        return auctions;
    }

    @Override
    public boolean analyzePrice(Integer auctionId, BigDecimal auctionPrice) {
        BigDecimal highestPrice = this.getById(auctionId).getHighestPrice();
        if(highestPrice == null){
            return true;
        }
        else if (auctionPrice.compareTo(highestPrice) == 1){
            return true;
        }
            return false;
    }
}
