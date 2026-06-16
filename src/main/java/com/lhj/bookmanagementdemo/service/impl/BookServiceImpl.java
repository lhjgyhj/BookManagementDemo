package com.lhj.bookmanagementdemo.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lhj.bookmanagementdemo.controller.request.BaseRequest;
import com.lhj.bookmanagementdemo.entity.Book;
import com.lhj.bookmanagementdemo.exception.ServiceException;
import com.lhj.bookmanagementdemo.mapper.BookMapper;
import com.lhj.bookmanagementdemo.service.IBookService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
public class BookServiceImpl implements IBookService {
    @Resource
    BookMapper bookMapper;
    @Override
    public List<Book> list() {
        return bookMapper.list();
    }

    @Override
    public PageInfo<Book> page(BaseRequest baseRequest) {
        PageHelper.startPage(baseRequest.getPageNum(),baseRequest.getPageSize());//获取页码和页数
        List<Book> obj = bookMapper.listByCondition(baseRequest);
        return new PageInfo<>(obj);

    }

    @Override
    public void save(Book obj) {
        try{
            obj.setCategory(category(obj.getCategories()));

            bookMapper.save(obj);
        }catch (Exception e){
            throw new ServiceException("添加图书失败",e);
        }

    }

    @Override
    public Book getById(Integer id) {
        return bookMapper.getById(id);
    }

    @Override
    public void updateById(Book obj) {
        try {
            obj.setCategory(category(obj.getCategories()));
            obj.setUpdatetime(LocalDate.now());
            bookMapper.updateById(obj);

        }catch (Exception e){
            throw new ServiceException("更新图书失败",e);

        }

    }

    @Override
    public void deleteById(Integer id) {
        bookMapper.deleteById(id);

    }

    private  String category(List<String> categories){
        StringBuilder sb = new StringBuilder();
        if (CollUtil.isNotEmpty( categories)){
            categories.forEach(v -> sb.append(v).append(" > "));
           return sb.substring(0,sb.lastIndexOf(" > "));
        }
        return sb.toString();
    }

}
