package com.zbw.blog.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author 17587
 */
@Data
@TableName(value = "user")
public class User implements Serializable {
    /**
     * 主键id
     */
    private Long id;
    /**
     * 创建时间
     */
    private Timestamp createTime;
    /**
     * 更新时间
     */
    private Timestamp updateTime;
    /**
     * 账号
     */
    private String account;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 密码
     */
    private String password;
    /**
     * 昵称
     */
    private String userName;
    /**
     * 头像
     */
    private String avatar;
    /**
     * 性别
     */
    private Character gender;
    /**
     * 签名
     */
    private String sign;
    /**
     * 出生日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Timestamp birthday;
    /**
     * 是否启用,0未启用,1启用
     */
    private Integer isEnabled;
    /**
     * 账号是否过期,0未过期,1过期
     */
    private Integer isAccountExpired;
    /**
     *是否锁定,0未锁定,1锁定
     */
    private Integer isLocked;
    /**
     * 密码是否过期,0未过期,1过期
     */
    private Integer isPasswordExpired;

    @TableField(exist = false)
    public static String TAG = "USER_TAG";
    @TableField(exist = false)
    private static final long serialVersionUID = 3L;
}
