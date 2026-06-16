package com.lhj.bookmanagementdemo.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lhj.bookmanagementdemo.controller.request.BaseRequest;
import com.lhj.bookmanagementdemo.entity.Book;
import com.lhj.bookmanagementdemo.entity.Borrow;
import com.lhj.bookmanagementdemo.entity.User;
import com.lhj.bookmanagementdemo.exception.ServiceException;
import com.lhj.bookmanagementdemo.mapper.BookMapper;
import com.lhj.bookmanagementdemo.mapper.BorrowMapper;
import com.lhj.bookmanagementdemo.mapper.UserMapper;
import com.lhj.bookmanagementdemo.mapper.po.BorrowReturnCountPo;
import com.lhj.bookmanagementdemo.service.IBorrowService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;


@Slf4j
@Service
public class BorrowServiceImpl implements IBorrowService {
    @Resource
    BorrowMapper borrowMapper;
    @Autowired
    UserMapper userMapper;
    @Autowired
    BookMapper bookMapper;
    @Override
    public List<Borrow> list() {
        return borrowMapper.list();
    }

    @Override
    public PageInfo<Borrow> page(BaseRequest baseRequest) {
        PageHelper.startPage(baseRequest.getPageNum(),baseRequest.getPageSize());//获取页码和页数
        List<Borrow> obj = borrowMapper.listByCondition(baseRequest);
        //提醒功能开发
        for (Borrow borrow : obj){
            LocalDate returnDate = borrow.getReturnDate();
            LocalDate now =LocalDate.now();
            if (returnDate == null || now.plusDays(3).isEqual(returnDate)){
                borrow.setNote(returnDate == null ? "未设置归还日期" : "即将到期");
            }else if (now.isEqual(returnDate)){
                borrow.setNote("已到期");
            }else if (now.isAfter(returnDate)){
                borrow.setNote("已过期");
            }else {
                borrow.setNote("未到期");
            }
        }
        return new PageInfo<>(obj);

    }

    @Override
    @Transactional
    public void save(Borrow obj) {
        //校验用户的积分是否足够
        String username =obj.getUserId();
        User user = userMapper.getByUserName(username);
        if (Objects.isNull(user)){
            throw new ServiceException("用户不存在");
        }
        //校验图书的数量是否足够
        Book book = bookMapper.getByBookNo(obj.getBookNo());
        if (Objects.isNull(book)){
            throw new ServiceException("所借图书不存在");
        }


        Integer account = user.getAccount();
        Integer score = book.getScore();
        //校验用户余额
        if (account<score){
            throw new ServiceException("用户积分不足");
        }
        //校验图书数量
        if (book.getNums()<1){
            throw new ServiceException("所借图书数量不足");
        }
      //更新余额
        user.setAccount(account-score);
        userMapper.updateById(user);
        //更新图书数量
        book.setNums(book.getNums()-1);
        bookMapper.updateById(book);
        obj.setReturnDate(LocalDate.now().plus(obj.getDays(), ChronoUnit.DAYS));//当前的日期加days得到归还的日期
        //新增借书记录
        borrowMapper.save(obj);
    }

    @Override
    public Borrow getById(Integer id) {
        return borrowMapper.getById(id);
    }

    @Override
    public void updateById(Borrow obj) {
        obj.setUpdatetime(LocalDate.now());
        borrowMapper.updateById(obj);

    }

    @Override
    public void deleteById(Integer id) {
        borrowMapper.deleteById(id);

    }
    @Override
    public boolean renewBorrow(Integer id, Integer extendDays) {
        if (extendDays == null || extendDays <= 0) {
            extendDays = 10;
        }
        borrowMapper.renewBorrow(id, extendDays, LocalDate.now().toString());
        Borrow borrow = borrowMapper.getById(id);
        return borrow != null && borrow.getReturnDate().isAfter(LocalDate.now());
    }
    @Override
    public Map<String, Object> getCountByTimeRange(String timeRange) {
        Map<String, Object> map = new HashMap<>();
        Date today = new Date();
        List<DateTime> dateRange = new ArrayList<>();
//ofsetDay(today, -6)计算时间的方法（天）
        //rangeToList(start, end, field)返回从开始时间到结束时间的一个时间范围
        switch (timeRange) {
            case "week":
                dateRange = DateUtil.rangeToList(DateUtil.offsetDay(today, -6), today, DateField.DAY_OF_MONTH);
                break;
            case "month":
                dateRange = DateUtil.rangeToList(DateUtil.offsetDay(today, -29), today, DateField.DAY_OF_MONTH);
                break;
            case "quarter":
                dateRange = DateUtil.rangeToList(DateUtil.offsetDay(today, -89), today, DateField.DAY_OF_MONTH);
                break;
            case "year":
                dateRange = DateUtil.rangeToList(DateUtil.offsetMonth(today, -11), today, DateField.MONTH);
                break;
            default:
                dateRange = DateUtil.rangeToList(DateUtil.offsetDay(today, -6), today, DateField.DAY_OF_MONTH);
                break;
        }
//datetimeToDateStr（）处理方法 把DateTime类型的list转为String
        List<String> dateStrRange = datetimeToDateStr(dateRange);
        map.put("date", dateStrRange);//x轴日期数据完毕
//timeRange为week、month、quarter、year时，查询借阅和归还的记录
        //getCountByTimeRange不会统计数据库没有的日期 比如 12.1没有借阅记录，那么他不会返回date=2025-12-01，count=0这个数据
        List<BorrowReturnCountPo> borrowCount = borrowMapper.getCountByTimeRange(timeRange, 1);//borrow
        List<BorrowReturnCountPo> returnCount = borrowMapper.getCountByTimeRange(timeRange, 2);//retur

        map.put("borrow", countList(borrowCount, dateStrRange));
        map.put("retur", countList(returnCount, dateStrRange));

        return map;
    }
    private List<String> datetimeToDateStr(List<DateTime> dateTimeList) {
        List<String> list = CollUtil.newArrayList();
        if (CollUtil.isNotEmpty(dateTimeList)) {
            for (DateTime dateTime : dateTimeList) {
                String date = DateUtil.formatDate(dateTime);
                list.add(date);
            }
        }
        return list;
    }
    //对数据库未统计的时间进行处理
    private List<Integer> countList(List<BorrowReturnCountPo> countPOList, List<String> dateRange) {
        List<Integer> list = CollUtil.newArrayList();
        if (CollUtil.isEmpty(countPOList)) {
            return list;
        }
        for (String date : dateRange) {
            Integer count = countPOList.stream()
                    .filter(countPO -> date.equals(countPO.getDate()))//
                    .map(BorrowReturnCountPo::getCount)//取出对象里的count值
                    .findFirst()//获取第一个结果
                    .orElse(0);//找不到就返回0
            list.add(count);
        }
        // 最后返回的list的元素个数会跟dateRange的元素个数完全一样
//        dateRange: [
//        "2022-10-30",
//                "2022-10-31",
//                "2022-11-01",
//                "2022-11-02",
//                "2022-11-03",
//                "2022-11-04",
//                "2022-11-05"
//],
//        borrow: [
//        0,
//                0,
//                2,
//                1,
//                0,
//                1,
//                3
//]
        return list;
    }
}
