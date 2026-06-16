package com.lhj.bookmanagementdemo.controller;
//将数据传输给前端

import cn.hutool.core.collection.CollUtil;
import com.lhj.bookmanagementdemo.common.Result;
import com.lhj.bookmanagementdemo.controller.request.CategoryPageRequest;
import com.lhj.bookmanagementdemo.entity.Category;
import com.lhj.bookmanagementdemo.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin//解决跨域错误

@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    ICategoryService categoryService;
    @GetMapping("list")
    public Result list() {
        List<Category> categorys = categoryService.list();
        return Result.success(categorys);
    }
    @GetMapping("tree")
    public Result tree() {
        List<Category> list = categoryService.list();//获取所有分类
//        List<Category> treeList=list.stream().filter(v -> v.getPid()==null).collect(Collectors.toList());//获取一级分类

        return Result.success(createTree(null,list));//null表示从第一级开始递归
    }
    //完全递归的方法来实现递归树
    private List<Category> createTree(Integer pid, List<Category> categories) {
        List<Category> treeList = new ArrayList<>();
        for (Category category : categories) {
            if(pid==null){//第一级节点判断
                if (category.getPid()==null){
                    treeList.add(category);
                    category.setChildren(createTree(category.getId(), categories));
                }
            }
            else {
                if (pid.equals(category.getPid())) {
                    treeList.add(category);
                    category.setChildren(createTree(category.getId(), categories));
                }
            }
            if (CollUtil.isEmpty(category.getChildren())){
                category.setChildren(null);
            }
        }
        return treeList;
    }
    @GetMapping("{id}")
    public Result getById(@PathVariable Integer id) {
        Category category = categoryService.getById(id);
        return Result.success(category);
    }
    @GetMapping("/page")
    public Result page(CategoryPageRequest PageRequest){
        return Result.success(categoryService.page(PageRequest));
    }
    @PostMapping("/save")
    public Result save(@RequestBody/*将json转化为Category对象*/Category obj){

        categoryService.save(obj);
        return Result.success();
    }
    @PutMapping("/update")
    public Result update(@RequestBody/*将json转化为Category对象*/Category obj){
        categoryService.updateById(obj);
        return Result.success();
    }
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id){
        categoryService.deleteById(id);
        return Result.success();
    }
}
