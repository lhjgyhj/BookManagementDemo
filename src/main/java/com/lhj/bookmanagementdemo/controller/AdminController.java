package com.lhj.bookmanagementdemo.controller;
//将数据传输给前端

import com.lhj.bookmanagementdemo.common.Result;
import com.lhj.bookmanagementdemo.controller.dto.LoginDTO;
import com.lhj.bookmanagementdemo.controller.request.AdminPageRequest;
import com.lhj.bookmanagementdemo.controller.request.LoginRequest;
import com.lhj.bookmanagementdemo.controller.request.PasswordRequest;
import com.lhj.bookmanagementdemo.entity.Admin;
import com.lhj.bookmanagementdemo.service.IAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin//解决跨域错误

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    IAdminService adminService;
    @PostMapping("/login")
    public Result login(@RequestBody/*将json转化为admin对象*/LoginRequest request){
        LoginDTO loginDTO=adminService.login(request);
        return Result.success(loginDTO);
    }
    @PostMapping("/password")
    public Result password(@RequestBody/*将json转化为admin对象*/PasswordRequest request){
        adminService.changePass(request);
        return Result.success();
    }
    @GetMapping("list")
    public Result list() {
        List<Admin> admins = adminService.list();
        return Result.success(admins);
    }
    @GetMapping("{id}")
    public Result getById(@PathVariable Integer id) {
        Admin admin = adminService.getById(id);
        return Result.success(admin);
    }
    @GetMapping("/page")
    public Result page(AdminPageRequest adminPageRequest){
        return Result.success(adminService.page(adminPageRequest));
    }
    @PostMapping("/save")
    public Result save(@RequestBody/*将json转化为admin对象*/Admin obj){

        adminService.save(obj);
        return Result.success();
    }
    @PutMapping("/update")
    public Result update(@RequestBody/*将json转化为admin对象*/Admin obj){
        adminService.updateById(obj);
        return Result.success();
    }
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id){
        adminService.deleteById(id);
        return Result.success();
    }
}
