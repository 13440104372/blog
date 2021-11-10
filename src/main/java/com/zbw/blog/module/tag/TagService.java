package com.zbw.blog.module.tag;

import com.zbw.blog.pojo.Tag;

import java.util.List;

/**
 * @author 17587
 */
public interface TagService {

    /**
     * 通过名称关键字查找标签
     * @param key 关键字
     * @return 标签列表
     */
    List<Tag> findTagByKey(String key);

    /**
     * 通过名称关键字查找标签
     * @param key 关键字
     * @return 标签列表
     */
    List<Tag> findTagByKeyWithExcludes(String key,List<Tag> tags);
}
