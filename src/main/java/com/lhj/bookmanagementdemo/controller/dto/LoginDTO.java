package com.lhj.bookmanagementdemo.controller.dto;

import lombok.Data;
//封装返回的数据，登录界面
@Data
public class LoginDTO {
    private Integer id;
    private String username;
    private String phone;
    private String email;
    private String token;
}
