package com.lhj.bookmanagementdemo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;

@Data
public class Borrow extends BaseEntity {
    private Integer bookNo;
    private String bookName;
    private String userName;
    private String userId;
    private String userPhone;
    private Integer score;
    private String status;
    private LocalDate returnDate;
    private Integer days;
    //提醒状态 即将到期（-1） 已到期（当天） 已过期（之后）
    private String note;
    private Integer renewTimes;
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private LocalDate renewTime;
}
