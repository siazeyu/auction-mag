package com.example.auctionmag.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;


// 底层就是spring mvc的拦截器机制

// D:\\pic 路径映射成网络地址路径

//  D:\\pic  -> http://localhost:8080    /uploads/cc.jpg   /uploads/**

// file: 固定写法  类似于 http: 访问协议
@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/uploads/**").addResourceLocations("file:/pic/");
		registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
	}

}
