package com.zbw.blog.module.essay;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zbw.blog.pojo.Essay;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 17587
 */
@Mapper
public interface EssayMapper extends BaseMapper<Essay> {

    /**
     * 添加文章
     *
     * @param essay 文章信息
     * @return 添加成功返回1
     */
    int addEssay(Essay essay);

    /**
     * 通过id查找文章
     * @param essayId 文章id
     * @return 文章
     */
    Essay findEssayById(@Param("essayId") Long essayId);

    /**
     * 通过id查找已发布的文章
     * @param essayId 文章id
     * @return 文章
     */
    Essay findReleasedEssayById(@Param("essayId") Long essayId);

    /**
     * 通过用户id查找已发布的文章
     *
     * @param userId 用户id
     * @param offset 偏移量
     * @param size   数据量
     * @return 已发布的文章列表
     */
    List<Essay> findReleasedEssayByUserIdWithLimit(@Param("userId") Long userId,
                                                   @Param("offset") Long offset,
                                                   @Param("size") Long size);

    /**
     * 获取用户已发布的文章数目
     *
     * @param userId 用户id
     * @return 文章数目
     */
    Long getCountForReleasedEssayByUserId(@Param("userId") Long userId);

    /**
     * 通过用户id查找未发布的文章
     *
     * @param userId 用户id
     * @param offset 偏移量
     * @param size   数据量
     * @return 已发布的文章列表
     */
    List<Essay> findUnReleasedEssayByUserIdWithLimit(@Param("userId") Long userId,
                                                     @Param("offset") Long offset,
                                                     @Param("size") Long size);

    /**
     * 获取用户未发布的文章数目
     *
     * @param userId 用户id
     * @return 文章数目
     */
    Long getCountForUnReleasedEssayByUserId(@Param("userId") Long userId);


    /**
     * 通过用户id查找已删除的文章
     *
     * @param userId 用户id
     * @param offset 偏移量
     * @param size   数据量
     * @return 已发布的文章列表
     */
    List<Essay> findDeletedEssayByUserIdWithLimit(@Param("userId") Long userId,
                                                     @Param("offset") Long offset,
                                                     @Param("size") Long size);

    /**
     * 获取用户已删除的文章数目
     *
     * @param userId 用户id
     * @return 文章数目
     */
    Long getCountForDeletedEssayByUserId(@Param("userId") Long userId);

    /**
     * 更新文章
     *
     * @param essay 文章信息
     * @return 添加成功返回1
     */
    int updateEssay(Essay essay);

    /**
     * 添加文章关联的标签
     *
     * @param essayId 文章id
     * @param tagId   标签id
     * @return 成功返回1
     */
    int addEssayTagRelation(@Param("essayId") Long essayId, @Param("tagId") Long tagId);

    /**
     * 删除文章关联的标签
     *
     * @param essayId 文章id
     * @param tagId   标签id
     * @return 成功返回1
     */
    int deleteEssayTagRelation(@Param("essayId") Long essayId, @Param("tagId") Long tagId);

    /**
     * 删除文章关联的所有标签
     *
     * @param essayId 文章id
     * @return 返回删除的条数
     */
    int deleteAllEssayTagRelations(@Param("essayId") Long essayId);

}
