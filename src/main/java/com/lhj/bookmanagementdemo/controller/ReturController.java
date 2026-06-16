package com.lhj.bookmanagementdemo.controller;

import com.lhj.bookmanagementdemo.common.Result;
import com.lhj.bookmanagementdemo.controller.request.BookPageRequest;
import com.lhj.bookmanagementdemo.controller.request.ReturPageRequest;
import com.lhj.bookmanagementdemo.entity.Borrow;
import com.lhj.bookmanagementdemo.entity.Retur;
import com.lhj.bookmanagementdemo.service.IReturService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin//解决跨域错误

@RestController
@RequestMapping("/retur")
public class ReturController {
    @Autowired
    IReturService returService;
    @PostMapping("/save")
    public Result save(@RequestBody/*将json转化为Borrow对象*/Retur obj){
        returService.save(obj);
        return Result.success();
    }
    @GetMapping("/page")
    public Result page(ReturPageRequest PageRequest){
        return Result.success(returService.page(PageRequest));
    }
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id){
        returService.deleteById(id);
        return Result.success();
    }
}
