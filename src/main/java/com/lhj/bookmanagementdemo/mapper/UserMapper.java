package com.lhj.bookmanagementdemo.mapper;

import com.lhj.bookmanagementdemo.controller.request.BaseRequest;
import com.lhj.bookmanagementdemo.controller.request.UserPageRequest;
import com.lhj.bookmanagementdemo.entity.Admin;
import com.lhj.bookmanagementdemo.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
@Mapper
public interface UserMapper {
//    @Select("select * from user")
    //查询所有对象
    List<User> list();

    List<User> listByCondition(BaseRequest baseRequest);

    void save(User  user);
    User getById(Integer id);

    void updateById(User user);

    void deleteById(Integer id);
    User getByUsernameAndPassword(@Param("username") String username, @Param("password") String password);

    User getByUserName(String username);
}
