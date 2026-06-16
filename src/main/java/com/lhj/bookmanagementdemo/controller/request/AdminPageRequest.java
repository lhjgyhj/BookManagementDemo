package com.lhj.bookmanagementdemo.controller.request;

import lombok.Data;

//特殊参数
@Data
public class AdminPageRequest extends BaseRequest{
    private String username;
    private String phone;
    private String email;

}
