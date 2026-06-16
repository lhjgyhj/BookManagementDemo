package com.lhj.bookmanagementdemo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.io.Serializable;


import java.time.LocalDate;
import java.util.List;

@Data
public class Book extends BaseEntity implements Serializable{
    private static final long serialVersionUID = 1L;//唯一编码

    private String name;
    private String author;
    private String description;
    private String cover;
    private String publisher;
    private String publishDate;
    private String category;
    private String bookNo;
    private List<String> categories;
    private Integer score;
    private Integer nums;

}
