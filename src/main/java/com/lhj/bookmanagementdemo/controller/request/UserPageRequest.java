package com.lhj.bookmanagementdemo.controller.request;

import lombok.Data;

//特殊参数
@Data
public class UserPageRequest extends BaseRequest{
    private String name;
    private String phone;

}
