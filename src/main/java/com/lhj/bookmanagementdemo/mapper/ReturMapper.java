package com.lhj.bookmanagementdemo.mapper;

import com.lhj.bookmanagementdemo.controller.request.BaseRequest;
import com.lhj.bookmanagementdemo.entity.Retur;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ReturMapper {
    List<Retur> listByCondition(BaseRequest baseRequest);
    void save(Retur  obj);
    void deleteById(Integer id);

    void updateStatus(String status,Integer id);
}
