package com.lhj.bookmanagementdemo.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lhj.bookmanagementdemo.controller.dto.LoginDTO;
import com.lhj.bookmanagementdemo.controller.request.BaseRequest;
import com.lhj.bookmanagementdemo.controller.request.LoginRequest;
import com.lhj.bookmanagementdemo.controller.request.UserPageRequest;
import com.lhj.bookmanagementdemo.entity.Admin;
import com.lhj.bookmanagementdemo.entity.User;
import com.lhj.bookmanagementdemo.exception.ServiceException;
import com.lhj.bookmanagementdemo.mapper.UserMapper;
import com.lhj.bookmanagementdemo.service.IUserService;
import com.lhj.bookmanagementdemo.utils.TokenUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
@Service
//实现接口
public class UserServiceImpl implements IUserService {
    @Autowired
    UserMapper userMapper;
    private static final String DEFAULT_PASSWORD = "123";
    private static final String PASS_SALT = "lhjuser";//盐
    @Override
    public List<User> list() {
        return userMapper.list();
    }
    @Override
    public LoginDTO login(LoginRequest request) {
        request.setPassword(securePassword(request.getPassword()));
        User user = userMapper.getByUsernameAndPassword(request.getUsername(), request.getPassword());
        if(user == null){
            throw new ServiceException("用户名或密码错误");
        }
        if(!user.isStatus()){
            throw new ServiceException("当前用户属于禁用状态 ");
        }
        LoginDTO loginDTO = new LoginDTO();
        BeanUtils.copyProperties(user, loginDTO);//将user对象中的数据复制给loginDTO对象,只复制公共属性
        String token = TokenUtils.genToken(String.valueOf(user.getId()), user.getPassword(),7);
        loginDTO.setToken(token);
        return loginDTO;
    }
//更新账户余额
    @Override
    public void handleAccount(User user) {
        Integer score = user.getScore();
        if (score == null){
            return;
        }
        Integer id = user.getId();
        User dbUser = userMapper.getById(id);//查
        dbUser.setAccount(dbUser.getAccount()+score);//加
        userMapper.updateById(dbUser);//更新
    }


    @Override
    public PageInfo<User> page(BaseRequest baseRequest) {
        PageHelper.startPage(baseRequest.getPageNum(),baseRequest.getPageSize());//获取页码和页数
        List<User> users = userMapper.listByCondition(baseRequest);
        return new PageInfo<>(users);

    }

    @Override
    public void save(User  user) {
        Date date =new Date();
        user.setUsername(DateUtil.format(date,"yyyyMMdd")+Math.abs/*abs取绝对值*/(IdUtil.fastSimpleUUID()/*uuid返回数字加编码*/.hashCode()/*将uuid返回的编码给转换成数字，使其卡号统一返回成数字（即有正数也有负数，要处理）*/));//会员卡号
        if (StrUtil.isBlank(user.getPassword())){
            user.setPassword(DEFAULT_PASSWORD);
        }
        user.setPassword(securePassword(user.getPassword()));
        userMapper.save(user);
    }

    @Override
    public User getById(Integer id) {
        return userMapper.getById(id);
    }

    @Override
    public void updateById(User user) {
        user.setUpdatetime(new Date());

        userMapper.updateById(user);
    }

    @Override
    public void deleteById(Integer id) {
        userMapper.deleteById(id);
    }
    private String securePassword(String password){//封装密码方法
        return SecureUtil.md5(password+PASS_SALT);//设置md5加密，加盐
    }

}
