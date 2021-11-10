package com.zbw.blog.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

/**
 * @author 17587
 */
@Data
@TableName(value = "essay")
public class Essay {
    private Long id;
    private Timestamp createTime;
    private Timestamp updateTime;
    private String subject;
    private String content;
    private Integer isReleased;
    private Integer isDeleted;
    private Long views;
    private Long likes;
    @TableField(exist = false)
    private List<Tag> tags;
    @TableField(exist = false)
    private User user;
}
