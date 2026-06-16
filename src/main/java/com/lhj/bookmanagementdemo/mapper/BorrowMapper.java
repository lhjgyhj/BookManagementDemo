package com.lhj.bookmanagementdemo.mapper;

import com.lhj.bookmanagementdemo.controller.request.BaseRequest;
import com.lhj.bookmanagementdemo.entity.Borrow;
import com.lhj.bookmanagementdemo.mapper.po.BorrowReturnCountPo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BorrowMapper {
    List<Borrow> list();
    List<Borrow> listByCondition(BaseRequest baseRequest);

    void save(Borrow  obj);

    Borrow getById(Integer id);

    void updateById(Borrow obj);

    void deleteById(Integer id);
    void renewBorrow(@Param("id") Integer id,
                     @Param("extendDays") Integer extendDays,
                     @Param("updatetime") String updatetime);
    List<BorrowReturnCountPo> getCountByTimeRange(@Param("timeRange") String timeRange, @Param("type") int type);

}

