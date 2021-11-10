package com.zbw.blog.module.tag;

import com.zbw.blog.pojo.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 17587
 */
@Service("tagService")
public class TagServiceImpl implements TagService{

    private TagMapper tagMapper;

    @Override
    public List<Tag> findTagByKey(String key) {
        return tagMapper.findTagByKey(key.toLowerCase());
    }

    @Override
    public List<Tag> findTagByKeyWithExcludes(String key,List<Tag> excludes) {
        return tagMapper.findTagByKeyWithExcludes(key.toLowerCase(),excludes);
    }

    @Autowired
    public void setTagMapper(TagMapper tagMapper) {
        this.tagMapper = tagMapper;
    }
}
