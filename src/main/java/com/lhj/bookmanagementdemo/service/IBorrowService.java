package com.lhj.bookmanagementdemo.service;


import com.github.pagehelper.PageInfo;
import com.lhj.bookmanagementdemo.controller.request.BaseRequest;
import com.lhj.bookmanagementdemo.entity.Borrow;

import java.util.List;
import java.util.Map;

public interface IBorrowService {
    List<Borrow> list();


    PageInfo<Borrow> page(BaseRequest baseRequest);

    void save(Borrow  obj);

    Borrow getById(Integer id);

    void updateById(Borrow obj);

    void deleteById(Integer id);

    boolean renewBorrow(Integer id, Integer extendDays);

    Map<String, Object> getCountByTimeRange(String timeRange);
}
