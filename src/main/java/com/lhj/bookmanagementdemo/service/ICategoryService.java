package com.lhj.bookmanagementdemo.service;

import com.github.pagehelper.PageInfo;
import com.lhj.bookmanagementdemo.controller.request.BaseRequest;
import com.lhj.bookmanagementdemo.entity.Category;

import java.util.List;

public interface ICategoryService {
    List<Category> list();


    PageInfo<Category> page(BaseRequest baseRequest);

    void save(Category  obj);

    Category getById(Integer id);

    void updateById(Category obj);

    void deleteById(Integer id);
}
