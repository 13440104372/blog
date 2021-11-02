package com.zbw.blog.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Timestamp;

@Data
@TableName(value = "user")
public class User {
    private Long id;
    private Timestamp createTime;
    private Timestamp updateTime;
    private int isDeleted;
    private String account;
    private String email;
    private String password;
    private int isActive;
    private String userName;
    private String avatar;
    private char gender;
    private String sign;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Timestamp birthday;
    @TableField(exist = false)
    private String token;
    @TableField(exist = false)
    private long expireAt;

    @TableField(exist = false)
    public static String TAG = "USER_TAG";
}
