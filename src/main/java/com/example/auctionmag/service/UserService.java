package com.example.auctionmag.service;

import com.example.auctionmag.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lishilin
 * @since 2022-05-22
 */
public interface UserService extends IService<User> {

    User handleLogin(String name, String password,String verificationCode);

    boolean handleAdminLogin(String name, String password,String verificationCode);

    void register(String userName, String password, String tel);
}
