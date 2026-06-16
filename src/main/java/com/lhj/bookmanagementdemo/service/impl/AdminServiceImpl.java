package com.lhj.bookmanagementdemo.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lhj.bookmanagementdemo.controller.dto.LoginDTO;
import com.lhj.bookmanagementdemo.controller.request.BaseRequest;
import com.lhj.bookmanagementdemo.controller.request.LoginRequest;
import com.lhj.bookmanagementdemo.controller.request.PasswordRequest;
import com.lhj.bookmanagementdemo.entity.Admin;
import com.lhj.bookmanagementdemo.exception.ServiceException;
import com.lhj.bookmanagementdemo.mapper.AdminMapper;
import com.lhj.bookmanagementdemo.service.IAdminService;
import com.lhj.bookmanagementdemo.utils.TokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Slf4j
@Service
//实现接口
public class AdminServiceImpl implements IAdminService {
    @Autowired
    AdminMapper adminMapper;
    private static final String DEFAULT_PASSWORD = "123";
    private static final String PASS_SALT = "lhj";//盐

    @Override
    public List<Admin> list() {

        return adminMapper.list();
    }

    @Override
    public PageInfo<Admin> page(BaseRequest baseRequest) {
        PageHelper.startPage(baseRequest.getPageNum(),baseRequest.getPageSize());//获取页码和页数
        List<Admin> obj = adminMapper.listByCondition(baseRequest);
        return new PageInfo<>(obj);

    }

    @Override
    public void save(Admin obj) {
        if (StrUtil.isBlank(obj.getPassword())){
            //默认密码123
            obj.setPassword(DEFAULT_PASSWORD);
        }
        obj.setPassword(securePassword(obj.getPassword()));
        try {
            adminMapper.save(obj);
        } catch (DuplicateKeyException e){
            log.error("保存用户信息出错,username:{}",obj.getUsername());
            throw new  ServiceException("用户插入失败,用户名重复");
        }
    }

    @Override
    public Admin getById(Integer id) {
        return adminMapper.getById(id);
    }

    @Override
    public void updateById(Admin obj) {
        obj.setUpdatetime(new Date());
        adminMapper.updateById(obj);
    }

    @Override
    public void deleteById(Integer id) {
        adminMapper.deleteById(id);
    }

    @Override
    public LoginDTO login(LoginRequest request) {
        adminMapper.getByUsername(request.getUsername());
        Admin admin = null;
        try{
         admin = adminMapper.getByUsername(request.getUsername());
    } catch (Exception e) {
            log.error("根据用户名{}查询出错",request.getUsername());
            throw new ServiceException("用户名错误");
        }
        if(admin == null){
            throw new ServiceException("用户名或密码错误");
        }//判断密码是否合法
        String securePassword = securePassword(request.getPassword());
        if(!securePassword.equals(admin.getPassword())){
            throw new ServiceException("用户名或密码错误");
        }
        LoginDTO loginDTO = new LoginDTO();
        BeanUtils.copyProperties(admin, loginDTO);//将admin对象中的数据复制给loginDTO对象,只复制公共属性
        String token =TokenUtils.genToken(String.valueOf(admin.getId()), admin.getPassword(),7);
        loginDTO.setToken(token);
        return loginDTO;
    }

    @Override
    public void changePass(PasswordRequest request) {
        request.setNewPass(securePassword(request.getNewPass()));//对新的密码进行加密
        int count = adminMapper.updatePassword(request.getNewPass(), request.getUsername());
        if (count<=0){
            throw new ServiceException("密码修改失败");
        }
    }

    private String securePassword(String password){//封装密码方法
        return SecureUtil.md5(password+PASS_SALT);//设置md5加密，加盐
    }



}