package com.lhj.bookmanagementdemo.mapper;

import com.lhj.bookmanagementdemo.controller.request.BaseRequest;
import com.lhj.bookmanagementdemo.controller.request.LoginRequest;
import com.lhj.bookmanagementdemo.controller.request.UserPageRequest;
import com.lhj.bookmanagementdemo.entity.Admin;
import com.lhj.bookmanagementdemo.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AdminMapper {
//    @Select("select * from user")
    //查询所有对象
    List<Admin> list();

    List<Admin> listByCondition(BaseRequest baseRequest);

    void save(Admin  obj);

    Admin getById(Integer id);

    void updateById(Admin obj);

    void deleteById(Integer id);

    Admin getByUsernameAndPassword(@Param("username") String username,@Param("password") String password);

    int updatePassword(@Param("newPass") String newPass,@Param("username") String username);

    Admin getByUsername(String username);
}
