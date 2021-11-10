package com.zbw.blog.module.tag;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zbw.blog.pojo.Tag;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 17587
 */
@Mapper
public interface TagMapper extends BaseMapper<Tag> {

    /**
     * 名称关键字查找标签
     * @param key 关键字
     * @return 标签列表
     */
    List<Tag> findTagByKey(@Param("key") String key);

    /**
     * 名称关键字查找标签
     * @param key 关键字
     * @param excludes 不包括的标签
     * @return 标签列表
     */
    List<Tag> findTagByKeyWithExcludes(@Param("key") String key,@Param("excludes") List<Tag> excludes);
}
