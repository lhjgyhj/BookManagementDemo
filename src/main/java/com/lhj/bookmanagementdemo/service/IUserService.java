package com.lhj.bookmanagementdemo.service;

import com.github.pagehelper.PageInfo;
import com.lhj.bookmanagementdemo.controller.dto.LoginDTO;
import com.lhj.bookmanagementdemo.controller.request.BaseRequest;
import com.lhj.bookmanagementdemo.controller.request.LoginRequest;
import com.lhj.bookmanagementdemo.controller.request.UserPageRequest;
import com.lhj.bookmanagementdemo.entity.User;

import java.util.List;

//提供方法
public interface IUserService {
    List<User> list();


    PageInfo<User> page(BaseRequest baseRequest);

    void save(User  user);

    User getById(Integer id);

    void updateById(User user);

    void deleteById(Integer id);
    LoginDTO login(LoginRequest request);

    void handleAccount(User user);
}
