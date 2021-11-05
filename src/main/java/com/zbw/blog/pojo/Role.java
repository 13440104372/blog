package com.zbw.blog.pojo;

import lombok.Data;
import org.apache.commons.net.ntp.TimeStamp;

/**
 * 角色
 * @author 17587
 */
@Data
public class Role {
    private Long id;
    private String name;
    private TimeStamp createTime;
    private String description;
}
