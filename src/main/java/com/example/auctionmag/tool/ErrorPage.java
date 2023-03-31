package com.example.auctionmag.tool;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

public class ErrorPage {

    @RequestMapping("/errorPage")
    public ModelAndView errorPage(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("errorMsg","404错误！");
        modelAndView.setViewName("error");
        return modelAndView;
    }
}
