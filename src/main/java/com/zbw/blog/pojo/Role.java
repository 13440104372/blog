package com.zbw.blog.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.apache.commons.net.ntp.TimeStamp;

import java.io.Serializable;

/**
 * 角色
 * @author 17587
 */
@Data
@TableName(value = "role")
public class Role implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private String name;
    private TimeStamp createTime;
    private TimeStamp updateTime;
    private String description;
}
