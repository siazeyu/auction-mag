package com.example.auctionmag.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.auctionmag.entity.User;
import com.example.auctionmag.mapper.UserMapper;
import com.example.auctionmag.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.auctionmag.tool.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ReactiveValueOperationsExtensionsKt;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lishilin
 * @since 2022-05-22
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Autowired
    RedisTemplate redisTemplate;

    @Override
    public User handleLogin(String name, String password,String verificationCode) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("userName",name);
        User user = this.baseMapper.selectOne(wrapper);
        if(user == null){
            System.out.println("user is not exit!");
        }
        else{
            if(user.getUserPassword().equals(password) && verificationCode.equals("7chh")){
                ValueOperations<String,User> ops = redisTemplate.opsForValue();
                //将用户名作为Key 值存入缓存中
                ops.set(name,user);
                redisTemplate.expire("name", 24*60, TimeUnit.MINUTES);//设置登录状态有效期为一天
                System.out.println("success");
                return user;               //login success!
            }
            else {
                System.out.println("password is error!");
            }
        }
        return null;
    }

    @Override
    public boolean handleAdminLogin(String name, String password,String verificationCode) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("userName",name);
        User user = this.baseMapper.selectOne(wrapper);
        if(user == null){
            System.out.println("user is not exit!");
        }
        else{
            if(user.getUserPassword().equals(password) && verificationCode.equals("7chh")){
                if (user.getUserIsadmin()==1) {
                    ValueOperations<String,Object> ops = redisTemplate.opsForValue();
                    ops.set("admin",user);
                    redisTemplate.expire("admin",24*60, TimeUnit.MINUTES);//设置登录状态有效期为一天
                    System.out.println("success login admin");
                    return true;               //login success!
                }
                else {
                    System.out.println("you are not Admin!");
                }
            }
            else {
                System.out.println("password is error!");
            }
        }
        return false;
    }

    @Override
    public void register(String userName, String password, String tel) {
        User user = new User();
        user.setUserName(userName);
        user.setUserPassword(password);
        user.setUserTel(tel);
        this.baseMapper.insert(user);
    }
}
