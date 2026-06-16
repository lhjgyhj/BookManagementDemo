package com.lhj.bookmanagementdemo.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lhj.bookmanagementdemo.controller.request.BaseRequest;
import com.lhj.bookmanagementdemo.entity.Borrow;
import com.lhj.bookmanagementdemo.entity.Retur;
import com.lhj.bookmanagementdemo.mapper.BookMapper;
import com.lhj.bookmanagementdemo.mapper.ReturMapper;
import com.lhj.bookmanagementdemo.service.IReturService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.beans.Transient;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class ReturServiceImpl implements IReturService {
    @Resource
    ReturMapper returMapper;
    @Resource
    BookMapper bookMapper;
    @Override
    public PageInfo<Retur> page(BaseRequest baseRequest) {
        PageHelper.startPage(baseRequest.getPageNum(),baseRequest.getPageSize());//获取页码和页数
        List<Retur> obj = returMapper.listByCondition(baseRequest);
        return new PageInfo<>(obj);
    }
    @Transactional
    @Override
    public void save(Retur obj) {
        //改状态
        obj.setStatus("已归还");

        returMapper.updateStatus("已归还",obj.getId());//修改借阅记录
        //图书数量增加
        obj.setRealDate(LocalDateTime.now());
        returMapper.save(obj);
        bookMapper.updateNumByNo(obj.getBookNo());
    }
    @Override
    public void deleteById(Integer id) {
        returMapper.deleteById(id);

    }
}
