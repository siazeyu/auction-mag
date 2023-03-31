package com.example.auctionmag.controller;


import com.example.auctionmag.service.AuctionrecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author lishilin
 * @since 2022-05-22
 */
@Controller
public class AuctionrecordController {

    @Autowired
    AuctionrecordService auctionrecordService;
    @RequestMapping("/auctionResult")
    public ModelAndView auctionRecordInfo(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("查看竞拍结果");
        return modelAndView;
    }
}

