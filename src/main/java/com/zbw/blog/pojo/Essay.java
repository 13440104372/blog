package com.zbw.blog.pojo;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class Essay {
    private int id;
    private Timestamp createTime;
    private Timestamp updateTime;
    private String content;
    private String subject;
    private int isReleased;
    private User user;
}
