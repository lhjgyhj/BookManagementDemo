package com.lhj.bookmanagementdemo.controller.request;

import lombok.Data;

//分页通用参数
//传参，相当于@RequestParam
@Data
public class BaseRequest {
    private Integer pageNum=1;//页码
    private Integer pageSize=10;//页数
}
