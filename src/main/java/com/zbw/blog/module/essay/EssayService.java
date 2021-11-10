package com.zbw.blog.module.essay;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zbw.blog.pojo.Essay;
import com.zbw.blog.pojo.Tag;

import java.util.List;

/**
 * @author 17587
 */
public interface EssayService {

    /**
     * 发布文章
     *
     * @param essay 文章
     * @return 发布成功返回true
     */
    boolean releaseEssay(Essay essay);

    /**
     * 保存到草稿
     * @param essay 文章草稿
     * @return 保存成功返回true
     */
    boolean preRelease(Essay essay);

    /**
     * 删除文章（将删除标记设为1）
     * @param essay 文章
     * @return 成功返回true
     */
    boolean deleteEssay(Essay essay);

    /**
     * 恢复文章（将删除标记设为0）
     * @param essay 文章
     * @return 成功返回true
     */
    boolean recoverEssay(Essay essay);

    /**
     * 移除文章（彻底删除）
     * @param essay 文章
     * @return 成功返回true
     */
    boolean removeEssay(Essay essay);

    /**
     * 通过用户id查找已发布的文章
     *
     * @param page 页码
     * @param size 数据量
     * @param userId 用户id
     * @return 已发布的文章列表
     */
    IPage<Essay> findReleasedEssayByUserIdWithPage(Long userId, Long page, Long size);

    /**
     * 通过用户id查找未发布的文章
     *
     * @param page 页码
     * @param size 数据量
     * @param userId 用户id
     * @return 已发布的文章列表
     */
    IPage<Essay> findUnReleasedEssayByUserIdWithPage(Long userId, Long page, Long size);

    /**
     * 通过用户id查找已删除的文章
     *
     * @param page 页码
     * @param size 数据量
     * @param userId 用户id
     * @return 已发布的文章列表
     */
    IPage<Essay> findDeletedEssayByUserIdWithPage(Long userId, Long page, Long size);

    /**
     * 为文章添加标签
     * @param essayId 文章id
     * @param tags 标签列表
     */
    void addEssayTagRelations(Long essayId, List<Tag> tags);

    /**
     * 为文章添加标签
     * @param essayId 文章id
     * @param tagId 标签id
     */
    boolean deleteEssayTagRelation(Long essayId, Long tagId);

    /**
     * 删除文章关联的所有标签
     *
     * @param essayId 文章id
     * @return 返回删除的条数
     */
    int deleteAllEssayTagRelations(Long essayId);
}
