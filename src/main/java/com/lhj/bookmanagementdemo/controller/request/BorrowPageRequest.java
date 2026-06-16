package com.lhj.bookmanagementdemo.controller.request;

import lombok.Data;

@Data
public class BorrowPageRequest extends BaseRequest {
    private Integer bookNo;
    private String bookName;
    private String userName;
    private String userId;
    private String userPhone;
}
