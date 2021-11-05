package com.zbw.blog.pojo;

import lombok.Data;

import java.io.Serializable;

/**
 * 权限
 * @author 17587
 */
@Data
public class Permission implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private String name;
    private String description;
}
