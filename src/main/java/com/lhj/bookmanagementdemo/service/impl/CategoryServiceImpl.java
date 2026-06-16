package com.lhj.bookmanagementdemo.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lhj.bookmanagementdemo.controller.request.BaseRequest;
import com.lhj.bookmanagementdemo.entity.Category;
import com.lhj.bookmanagementdemo.mapper.CategoryMapper;
import com.lhj.bookmanagementdemo.service.ICategoryService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class CategoryServiceImpl implements ICategoryService {
    @Resource
    CategoryMapper categoryMapper;

    @Override
    public List<Category> list() {

        return categoryMapper.list();
    }

    @Override
    public PageInfo<Category> page(BaseRequest baseRequest) {
        PageHelper.startPage(baseRequest.getPageNum(),baseRequest.getPageSize());//获取页码和页数
        List<Category> obj = categoryMapper.listByCondition(baseRequest);
        return new PageInfo<>(obj);

    }

    @Override
    public void save(Category obj) {
        categoryMapper.save(obj);
    }

    @Override
    public Category getById(Integer id) {
        return categoryMapper.getById(id);
    }

    @Override
    public void updateById(Category obj) {
        obj.setUpdatetime(LocalDate.now());
        categoryMapper.updateById(obj);

    }

    @Override
    public void deleteById(Integer id) {
        categoryMapper.deleteById(id);

    }

}
