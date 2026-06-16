package com.lhj.bookmanagementdemo.mapper;

import com.lhj.bookmanagementdemo.controller.request.BaseRequest;
import com.lhj.bookmanagementdemo.entity.Category;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CategoryMapper {
    List<Category> list();

    List<Category> listByCondition(BaseRequest baseRequest);

    void save(Category  obj);

    Category getById(Integer id);

    void updateById(Category obj);

    void deleteById(Integer id);

}
