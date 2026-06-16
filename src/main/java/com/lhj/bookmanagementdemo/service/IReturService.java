package com.lhj.bookmanagementdemo.service;

import com.github.pagehelper.PageInfo;
import com.lhj.bookmanagementdemo.controller.request.BaseRequest;
import com.lhj.bookmanagementdemo.entity.Borrow;
import com.lhj.bookmanagementdemo.entity.Retur;

public interface IReturService {
    PageInfo<Retur> page(BaseRequest baseRequest);

    void save(Retur  obj);
    void deleteById(Integer id);

}
