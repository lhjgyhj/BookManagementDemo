package com.lhj.bookmanagementdemo.controller.request;

import lombok.Data;

@Data
public class PasswordRequest {
    private String username;
    private String password;
    private String newPass;
}
