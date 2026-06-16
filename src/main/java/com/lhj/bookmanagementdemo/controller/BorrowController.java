package com.lhj.bookmanagementdemo.controller;

import com.lhj.bookmanagementdemo.common.Result;
import com.lhj.bookmanagementdemo.controller.request.BorrowPageRequest;
import com.lhj.bookmanagementdemo.entity.Borrow;
import com.lhj.bookmanagementdemo.service.IBorrowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@CrossOrigin//解决跨域错误

@RestController
@RequestMapping("/borrow")
public class BorrowController {
    @Autowired
    IBorrowService borrowService;
    static class RenewDTO {
        private Integer extendDays;
        public Integer getExtendDays() {return extendDays;} // 仅getter，无其他
    }
    @GetMapping("list")
    public Result list() {
        List<Borrow> Borrows = borrowService.list();
        return Result.success(Borrows);
    }
    @GetMapping("{id}")
    public Result getById(@PathVariable Integer id) {
        Borrow borrow = borrowService.getById(id);
        return Result.success(borrow);
    }
    @GetMapping("/page")
    public Result page(BorrowPageRequest PageRequest){
        return Result.success(borrowService.page(PageRequest));
    }
    @PostMapping("/save")
    public Result save(@RequestBody/*将json转化为Borrow对象*/Borrow obj){
        borrowService.save(obj);
        return Result.success();
    }
    @PutMapping("/update")
    public Result update(@RequestBody/*将json转化为Borrow对象*/Borrow obj){
        borrowService.updateById(obj);
        return Result.success();
    }
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id){
        borrowService.deleteById(id);
        return Result.success();
    }

    @PostMapping("/renew/{id}")
    public Result renew(@PathVariable Integer id, @RequestBody RenewDTO dto) {
        borrowService.renewBorrow(id, dto.getExtendDays()); // 直接转发，无任何判断
        return Result.success();
    }
    @GetMapping("/lineCharts/{timeRange}")
    public Result lineCharts(@PathVariable String timeRange) {
        return Result.success(borrowService.getCountByTimeRange(timeRange));
    }
}
