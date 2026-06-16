package com.lhj.bookmanagementdemo.controller;
//将数据传输给前端
import com.lhj.bookmanagementdemo.common.Result;
import com.lhj.bookmanagementdemo.controller.dto.LoginDTO;
import com.lhj.bookmanagementdemo.controller.request.LoginRequest;
import com.lhj.bookmanagementdemo.controller.request.UserPageRequest;
import com.lhj.bookmanagementdemo.entity.User;
import com.lhj.bookmanagementdemo.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin//解决跨域错误

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    IUserService userService;
    @PostMapping("/login")
    public Result login(@RequestBody/*将json转化为admin对象*/LoginRequest request){
        LoginDTO loginDTO=userService.login(request);
        return Result.success(loginDTO);
    }
    @GetMapping("list")
    public Result list() {
        List<User> users = userService.list();
        return Result.success(users);
    }
    @GetMapping("{id}")
    public Result getById(@PathVariable Integer id) {
        User user = userService.getById(id);
        return Result.success(user);
    }
    @GetMapping("/page")
    public Result page(UserPageRequest userPageRequest){
        return Result.success(userService.page(userPageRequest));
    }
    @PostMapping("/save")
    public Result save(@RequestBody/*将json转化为user对象*/User user){
        userService.save(user);
        return Result.success();
    }
    @PutMapping("/update")
    public Result update(@RequestBody/*将json转化为user对象*/User user){
        userService.updateById(user);
        return Result.success();
    }
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id){
        userService.deleteById(id);
        return Result.success();
    }
    @PostMapping("/account")
    public Result account(@RequestBody/*将json转化为user对象*/User user){
        userService.handleAccount(user);
        return Result.success();
    }
}
