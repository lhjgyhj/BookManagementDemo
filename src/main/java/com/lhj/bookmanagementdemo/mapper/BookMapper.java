package com.lhj.bookmanagementdemo.mapper;

import com.github.pagehelper.PageInfo;
import com.lhj.bookmanagementdemo.controller.request.BaseRequest;
import com.lhj.bookmanagementdemo.entity.Book;
import com.lhj.bookmanagementdemo.entity.Category;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BookMapper  {
    List<Book> list();
    List<Book> listByCondition(BaseRequest baseRequest);

    void save(Book  obj);

    Book getById(Integer id);

    void updateById(Book obj);

    void deleteById(Integer id);

    Book getByBookNo(Integer bookNo);

    void updateNumByNo(Integer bookNo);
}
