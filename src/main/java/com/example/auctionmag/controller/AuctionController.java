package com.example.auctionmag.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.auctionmag.entity.Auction;
import com.example.auctionmag.entity.Auctionrecord;
import com.example.auctionmag.entity.User;
import com.example.auctionmag.service.AuctionService;
import com.example.auctionmag.service.AuctionrecordService;
import com.example.auctionmag.tool.ErrorPage;
import com.example.auctionmag.tool.FileUtil;
import com.example.auctionmag.vo.AuctionRecordUserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author lishilin
 * @since 2022-05-22
 */
@Controller
public class AuctionController {

    @Autowired
    private AuctionService auctionService;
    @Autowired
    AuctionrecordService auctionrecordService;

    @Autowired
    RedisTemplate redisTemplate;
    private Integer auctionId;
    @Autowired
    UserController userController;

    /**
     * 获取拍卖信息
     * @return ModelAndView
     */
    @RequestMapping("/auctionMagPage")
    public ModelAndView manageAuction(Map<String,Object> map){
        ModelAndView modelAndView = new ModelAndView();
        List<Auction> auctions;
        if (map.isEmpty()) {
            auctions = auctionService.list();
            modelAndView.addObject("searchParam",new HashMap<>());
        }
        else{
            auctions = auctionService.searchAcution(map);
            modelAndView.addObject("searchParam", map);
        }
        User user;
        ValueOperations<String,User> ops = redisTemplate.opsForValue();
        user = ops.get("admin");  //查询redis 中是否存在user信息（相当于cookie）
        modelAndView.addObject("auctions",auctions);
        if (user!=null){
            modelAndView.setViewName("拍卖商品管理页");
            return modelAndView;  //处于登录状态
        }
        else {
            //管理员还未登录，必须先登录
            return new ModelAndView("竞拍者登录");
        }
    }

    /**
     * 删除拍卖信息
     * @param auctionId
     * @return
     */
    @RequestMapping("/AuctionDel/{auctionId}")
    public ModelAndView del(@PathVariable("auctionId") Integer auctionId){
        try {
            Integer id = auctionId;
            if (id != null)
                auctionService.removeById(id);
                System.out.println("成功删除商品");
        }
        catch (Exception e){
            System.out.println(e);
        }
        return this.manageAuction(new HashMap<>());
    }

    /**
     * 竞拍页，条件判断，登录状态实现
     * @return
     */
    @RequestMapping("/auctionPage")
    public ModelAndView auctionPage(@CookieValue(value = "userName") String redisKey){
        ModelAndView modelAndView = new ModelAndView();
        User user;
        ValueOperations<String,User> ops = redisTemplate.opsForValue();
        if (redisKey != null) {
            user = ops.get(redisKey);  //查询redis 中是否存在user信息（相当于cookie）
            if (user != null) {
                modelAndView.addObject("user", user);
                List<Auctionrecord> auctionrecords = auctionrecordService.list();
                List<AuctionRecordUserVo> auctionRecordUserVos = auctionService.getRecordUserName(auctionrecords);
                List<Auction> auctions = this.auctionService.seleteAuction();
                modelAndView.addObject("isAuctions", auctions);
                modelAndView.addObject("auctionRecordUser", auctionRecordUserVos);
                modelAndView.setViewName("竞拍页");
                return modelAndView;
            }
        }
        //没有登录状态
        return this.userController.login();
    }

    /**
     * 修改商品，传递商品Id给全局变量
     * @param auctionId
     * @return
     */
    @RequestMapping("/editAuctionPage/{auctionId}")
    public ModelAndView editAuctionPage(@PathVariable("auctionId") Integer auctionId){
        this.auctionId = auctionId;
        Auction auction = auctionService.getById(auctionId);
        ModelAndView modelAndView = new ModelAndView();
        //向修改页传递一个对象
        modelAndView.addObject("auction",auction);
        modelAndView.setViewName("修改商品页");
        return modelAndView;
    }

    /**
     * 处理修改请求
     * @param request
     * @return
     */
    @RequestMapping("/editAuction")
    public ModelAndView editAuction(HttpServletRequest request, @RequestParam("auctionPic") MultipartFile file){
        String auctionStartPrice = request.getParameter("auctionStartPrice");
        String auctionUpset = request.getParameter("auctionUpset");
        String auctionStartTime = request.getParameter("auctionStartTime");
        String auctionEndTime = request.getParameter("auctionEndTime");
        String auctionPic = UUID.randomUUID() + ".jpg";
        Auction auctionp = auctionService.getById(this.auctionId);
        Auction auction = new Auction();
        auction.setAuctionId(this.auctionId);
        if (file.getOriginalFilename().equals("")){
            auctionPic = auctionp.getAuctionPic();
        }else {
            FileUtil.save(file, auctionPic);
        }
        auction.setAuctionPic(auctionPic);
        auction.setAuctionStartPrice(new BigDecimal(auctionStartPrice));
        auction.setAuctionUpset(new BigDecimal(auctionUpset));
        auction.setAuctionStartTime(LocalDateTime.parse(auctionStartTime));
        auction.setAuctionEndTime(LocalDateTime.parse(auctionEndTime));
        this.auctionService.updateById(auction);
        System.out.println(auction.getAuctionName()+"修改成功");
        return this.manageAuction(new HashMap<>());
    }
    @RequestMapping("/addAuctionPage")
    public ModelAndView addAuctionPage(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("增加商品页");
        return modelAndView;
    }

    /**
     * 处理增加商品操作
     * @param request
     * @return
     */
    @RequestMapping("/addAuctionHandle")
    public ModelAndView addAuction(HttpServletRequest request, @RequestParam("auctionPic") MultipartFile file){
        String auctionPic = UUID.randomUUID() + ".jpg";
        FileUtil.save(file, auctionPic);
        String auctionName = request.getParameter("auctionName");
        String auctionStartPrice = request.getParameter("auctionStartPrice");
        String auctionUpset = request.getParameter("auctionUpset");
        String auctionStartTime = request.getParameter("auctionStartTime");
        String auctionEndTime = request.getParameter("auctionEndTime");
        String auctionDesc = request.getParameter("auctionDesc");
        Auction auction = new Auction();
        auction.setAuctionName(auctionName);
        auction.setAuctionStatus(2);//设置为未开始竞拍
        auction.setAuctionPic(auctionPic);
        auction.setAuctionPicType("image/jpg");
        auction.setAuctionDesc(auctionDesc);
        auction.setAuctionStartPrice(new BigDecimal(auctionStartPrice));
        auction.setAuctionUpset(new BigDecimal(auctionUpset));
        auction.setAuctionStartTime(LocalDateTime.parse(auctionStartTime));
        auction.setAuctionEndTime(LocalDateTime.parse(auctionEndTime));
        auction.setHighestPrice(new BigDecimal(auctionStartPrice));//最高价初始设置为起拍价
        auctionService.save(auction);
        System.out.println("成功保存竞拍品："+auctionName);
        QueryWrapper<Auction> wrapper = new QueryWrapper<>();
        //找出所有还没开始竞拍的商品，状态为2
        wrapper.eq("auctionStatus",2);
        List<Auction> auctions = this.auctionService.list(wrapper);
        //将为开始竞拍的商品加入到缓存中，用来监控开始时间
        ValueOperations<String,List<Auction>> ops = redisTemplate.opsForValue();
        //将auctionNotStart作为Key 值存入缓存中
        redisTemplate.delete("notStartAuction");
        ops.set("notStartAuction",auctions);
        System.out.println("成功更新redis 缓存");
        return this.manageAuction(new HashMap<>());
    }

    /**
     * 查询竞拍品
     * @param request
     * @return
     */
    @RequestMapping("/searchAuction")
    public ModelAndView searchAuction(HttpServletRequest request){
        String auctionName = request.getParameter("auctionName");
        String auctionDesc = request.getParameter("auctionDesc");
        String auctionStartPrice = request.getParameter("auctionStartPrice");
        Map<String,Object> map = new HashMap<>();
        map.put("auctionName",auctionName);
        map.put("auctionDesc",auctionDesc);
        map.put("auctionStartPrice",auctionStartPrice);
        return this.manageAuction(map);
    }

    /**
     * 参与竞拍
     * @param request
     * @return
     */
    @RequestMapping("/doAuction")
    public ModelAndView doAuction(HttpServletRequest request,@CookieValue(value = "userName") String redisKey){
        Integer auctionId = Integer.valueOf(request.getParameter("auctionId"));
        String price = request.getParameter("sale");
        BigDecimal auctionPrice = new BigDecimal(price);
        //判断提交的价格是否高于最高价
        boolean canAuction =  this.auctionService.analyzePrice(auctionId,auctionPrice);
        if (canAuction) {
            ValueOperations<String, User> ops = redisTemplate.opsForValue();
            User user = ops.get(redisKey);
            Integer userId = user.getUserId();//获取当前登录的用户Id
            LocalDateTime auctionTime = LocalDateTime.now();
            Auctionrecord auctionrecord = new Auctionrecord();
            auctionrecord.setAuctionId(auctionId);
            auctionrecord.setUserId(userId);
            auctionrecord.setAuctionTime(auctionTime);
            auctionrecord.setAuctionPrice(auctionPrice);
            //更新最高价
            Auction auction = auctionService.getById(auctionId);
            auction.setHighestPrice(auctionPrice);
            auctionService.saveOrUpdate(auction);
            auctionrecordService.save(auctionrecord);
            System.out.println("竞拍者："+user.getUserName());
        }
        return this.auctionPage(redisKey);
    }

}

