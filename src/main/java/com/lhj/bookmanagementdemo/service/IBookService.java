package com.lhj.bookmanagementdemo.service;

import com.github.pagehelper.PageInfo;
import com.lhj.bookmanagementdemo.controller.request.BaseRequest;
import com.lhj.bookmanagementdemo.entity.Book;
import com.lhj.bookmanagementdemo.entity.Category;

import java.util.List;

public interface IBookService {
    List<Book> list();


    PageInfo<Book> page(BaseRequest baseRequest);

    void save(Book  obj);

    Book getById(Integer id);

    void updateById(Book obj);

    void deleteById(Integer id);
}
