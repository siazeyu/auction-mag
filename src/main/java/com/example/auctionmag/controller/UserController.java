package com.example.auctionmag.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.auctionmag.entity.Auction;
import com.example.auctionmag.entity.User;
import com.example.auctionmag.service.AuctionService;
import com.example.auctionmag.service.UserService;
import com.example.auctionmag.tool.ErrorPage;
import com.example.auctionmag.tool.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.jws.soap.SOAPBinding;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author lishilin
 * @since 2022-05-22
 */
@RestController
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    AuctionService auctionService;
    @Autowired
    AuctionController auctionController;
    @Autowired
    RedisTemplate redisTemplate;

    @RequestMapping("/")
    public ModelAndView login(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("竞拍者登录");
        return modelAndView;
    }
    @GetMapping("list")
    public List<User> list(){
        return userService.list();
    }
    //竞拍者登录处理
    @RequestMapping("/handleUserLogin")
    public ModelAndView userLogin(HttpServletRequest request, HttpServletResponse response){
        String name = request.getParameter("name");
        String password = request.getParameter("password");
        String verificationCode = request.getParameter("verificationCode");
        User user = this.userService.handleLogin(name,password,verificationCode);
        if(user != null){
            System.out.println("登录成功");
            // 创建一个 cookie对象
            Cookie cookie = new Cookie("userName", user.getUserName());
            cookie.setMaxAge(24 * 60 * 60); // cookie1天后过期
            //将cookie对象加入response响应
            response.addCookie(cookie);
            return auctionController.auctionPage(user.getUserName());//登录成功，进入竞拍页
        }
        else {
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.setViewName("竞拍者登录");
            modelAndView.getModel().put("msg","密码错误！");
            return modelAndView;
        }
    }
    //注销
    @RequestMapping("/logout")
    public ModelAndView logout(@CookieValue(value = "userName",defaultValue = "admin") String userName, HttpServletResponse response){
        //将用户从缓存中移除,并将cookie移除
        if (userName != null) {
            if (redisTemplate.hasKey(userName)) {
                // 将Cookie的值设置为null
                Cookie cookie = new Cookie("userName", null);
                //将`Max-Age`设置为0
                cookie.setMaxAge(0);
                response.addCookie(cookie);
                redisTemplate.delete(userName);
            }
        }
        //将管理员从缓存中移除
        if (redisTemplate.hasKey("admin")){
            redisTemplate.delete("admin");
        }

        return login();
    }
    //admin to Login
    @RequestMapping("/adminLogin")
    public ModelAndView loginAdmin(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("管理员登录页");
        return modelAndView;
    }
    @RequestMapping("/adminLoginHandle")
    public ModelAndView adminLoginHandle(HttpServletRequest request){
        String name = request.getParameter("name");
        String password = request.getParameter("password");
        String verificationCode = request.getParameter("verificationCode");
        boolean flag = this.userService.handleAdminLogin(name,password,verificationCode);
        if (flag){
            return this.auctionController.manageAuction(new HashMap<>());//商品管理页
        }
        else {
            return this.login();
        }
    }
    //注册页
    @RequestMapping("registerPage")
    public ModelAndView registerPage(){
        return new ModelAndView("竞拍者注册页");
    }
    //注册处理
    @RequestMapping("/register")
    public ModelAndView register(HttpServletRequest request){
        String userName = request.getParameter("userName");
        String password = request.getParameter("pwd");
        String tel = request.getParameter("tel");
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("userName",userName);
        User user = userService.getOne(wrapper);
        if (user == null) {  //不存在重复用户名，给予注册
            if (password.equals(request.getParameter("pwd2"))) {
                this.userService.register(userName, password, tel);
            }
            System.out.println("注册成功");
            return this.login();
        }
        else {
            System.out.println("用户已存在，注册失败");
            return new ModelAndView("error");
        }
    }
}

