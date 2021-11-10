package com.zbw.blog.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.apache.commons.net.ntp.TimeStamp;

/**
 * 文章标签
 * @author 17587
 */
@Data
@TableName(value = "tag")
public class Tag {
    private Long id;
    private String name;
    private TimeStamp createTime;
    private TimeStamp updateTime;
    private String description;
}
