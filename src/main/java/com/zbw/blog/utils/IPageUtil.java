package com.zbw.blog.utils;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * @author a1758712122
 */
public class IPageUtil {

    public static <T> IPage<T> getPage(Long page, Long size, Long total) {
        IPage<T> result = new Page<>();
        if (page < 1L) {
            page = 1L;
        }
        result.setCurrent(page);
        result.setSize(size);
        result.setTotal(total);
        result.setPages(total / size + (total % size == 0 ? 0 : 1));
        return result;
    }
}
