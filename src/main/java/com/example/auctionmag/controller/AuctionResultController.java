package com.example.auctionmag.controller;


import com.example.auctionmag.entity.AuctionResult;
import com.example.auctionmag.entity.Auctionrecord;
import com.example.auctionmag.entity.User;
import com.example.auctionmag.service.AuctionResultService;
import com.example.auctionmag.service.AuctionrecordService;
import com.example.auctionmag.vo.AuctionRecordUserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author lishilin
 * @since 2022-05-26
 */
@Controller
public class AuctionResultController {
    @Autowired
    private AuctionResultService auctionResultService;
    @Autowired
    private AuctionrecordService auctionrecordService;
    @Autowired
    RedisTemplate redisTemplate;

    //竞拍结果页面
    @RequestMapping("/auctionResultPage")
    public ModelAndView auctionResultPage(@CookieValue(value = "userName",defaultValue = "lishilin") String redisKey){
        ModelAndView modelAndView = new ModelAndView();
        ValueOperations<String, User> ops = redisTemplate.opsForValue();
        User user = ops.get(redisKey);
        modelAndView.addObject("user",user);
        List<AuctionResult> auctionResults = this.auctionResultService.list();
        modelAndView.addObject("auctionResults",auctionResults);
        //获取竞拍中的商品的竞拍记录,嵌套集合
        List<Auctionrecord> auctionrecords = this.auctionrecordService.getAuctionRecord();
        //根据所有记录封装一个vo返回前端
        List<AuctionRecordUserVo> auctionRecordUserVos = auctionResultService.getUserVo(auctionrecords);
        System.out.println("封装的竞拍记录："+auctionRecordUserVos);
        modelAndView.addObject("auctionRecordUserVos",auctionRecordUserVos);
        modelAndView.setViewName("查看竞拍结果");
        return modelAndView;
    }
}

