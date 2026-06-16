package com.lhj.bookmanagementdemo.service;

import com.github.pagehelper.PageInfo;
import com.lhj.bookmanagementdemo.controller.dto.LoginDTO;
import com.lhj.bookmanagementdemo.controller.request.BaseRequest;
import com.lhj.bookmanagementdemo.controller.request.LoginRequest;
import com.lhj.bookmanagementdemo.controller.request.PasswordRequest;
import com.lhj.bookmanagementdemo.entity.Admin;

import java.util.List;

//提供方法
public interface IAdminService {
    List<Admin> list();


    PageInfo<Admin> page(BaseRequest baseRequest);

    void save(Admin  obj);

    Admin getById(Integer id);

    void updateById(Admin obj);

    void deleteById(Integer id);

    LoginDTO login(LoginRequest request);

    void changePass(PasswordRequest request);
}
