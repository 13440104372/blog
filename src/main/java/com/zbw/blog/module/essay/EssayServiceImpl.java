package com.zbw.blog.module.essay;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.zbw.blog.pojo.Essay;
import com.zbw.blog.pojo.Tag;
import com.zbw.blog.utils.IPageUtil;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 17587
 */
@Service("essayService")
public class EssayServiceImpl implements EssayService {

    private EssayMapper essayMapper;
    private SqlSessionFactory sqlSessionFactory;

    @Override
    public boolean releaseEssay(Essay essay) {
        essay.setIsReleased(1);
        return insert(essay);
    }

    @Override
    public boolean preRelease(Essay essay) {
        essay.setIsReleased(0);
        return insert(essay);
    }

    private boolean insert(Essay essay) {
        Essay local = essayMapper.findEssayById(essay.getId());
        if (local == null) {
            if (essayMapper.addEssay(essay) > 0) {
                addEssayTagRelations(essay.getId(), essay.getTags());
                return true;
            }
        } else {
            if (essayMapper.updateEssay(essay) > 0) {
                addEssayTagRelations(essay.getId(), essay.getTags());
                return true;
            }
        }
        return false;
    }

    @Override
    public IPage<Essay> findReleasedEssayByUserIdWithPage(Long userId, Long page, Long size) {
        IPage<Essay> essayPage = IPageUtil.getPage(page, size, essayMapper.getCountForReleasedEssayByUserId(userId));
        long offset = (page - 1) * size;
        if (offset < 0) {
            offset = 0;
        }
        List<Essay> results = essayMapper.findReleasedEssayByUserIdWithLimit(userId, offset, size);
        essayPage.setRecords(results);
        return essayPage;
    }

    @Override
    public IPage<Essay> findUnReleasedEssayByUserIdWithPage(Long userId, Long page, Long size) {
        IPage<Essay> essayPage = IPageUtil.getPage(page, size, essayMapper.getCountForUnReleasedEssayByUserId(userId));
        long offset = (page - 1) * size;
        if (offset < 0) {
            offset = 0;
        }
        List<Essay> results = essayMapper.findUnReleasedEssayByUserIdWithLimit(userId, offset, size);
        essayPage.setRecords(results);
        return essayPage;
    }

    @Override
    public IPage<Essay> findDeletedEssayByUserIdWithPage(Long userId, Long page, Long size) {
        IPage<Essay> essayPage = IPageUtil.getPage(page, size, essayMapper.getCountForDeletedEssayByUserId(userId));
        long offset = (page - 1) * size;
        if (offset < 0) {
            offset = 0;
        }
        List<Essay> results = essayMapper.findDeletedEssayByUserIdWithLimit(userId, offset, size);
        essayPage.setRecords(results);
        return essayPage;
    }

    @Override
    public void addEssayTagRelations(Long essayId, List<Tag> tags) {
        if (tags.size() == 0) {
            return;
        }
        SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH);
        EssayMapper mapper = sqlSession.getMapper(EssayMapper.class);
        tags.forEach(tag -> mapper.addEssayTagRelation(essayId, tag.getId()));
        sqlSession.commit();
        sqlSession.clearCache();
        sqlSession.close();
    }

    @Override
    public boolean deleteEssayTagRelation(Long essayId, Long tagId) {
        return essayMapper.deleteEssayTagRelation(essayId,tagId)>0;
    }

    @Override
    public int deleteAllEssayTagRelations(Long essayId) {
        return essayMapper.deleteAllEssayTagRelations(essayId);
    }

    @Override
    public boolean deleteEssay(Essay essay) {
        LambdaUpdateWrapper<Essay> essayLambdaUpdateWrapper = Wrappers.lambdaUpdate();
        essayLambdaUpdateWrapper.eq(Essay::getId,essay.getId()).set(Essay::getIsDeleted,1);
        return essayMapper.update(null,essayLambdaUpdateWrapper)>0;
    }

    @Override
    public boolean recoverEssay(Essay essay) {
        LambdaUpdateWrapper<Essay> essayLambdaUpdateWrapper = Wrappers.lambdaUpdate();
        essayLambdaUpdateWrapper.eq(Essay::getId,essay.getId()).set(Essay::getIsDeleted,0);
        return essayMapper.update(null,essayLambdaUpdateWrapper)>0;
    }

    @Override
    public boolean removeEssay(Essay essay) {
        LambdaQueryWrapper<Essay> essayLambdaQueryWrapper = Wrappers.lambdaQuery();
        essayLambdaQueryWrapper.eq(Essay::getId,essay.getId());
        if(essayMapper.delete(essayLambdaQueryWrapper)>0){
            deleteAllEssayTagRelations(essay.getId());
            return true;
        }
        return false;
    }

    @Autowired
    public void setEssayMapper(EssayMapper essayMapper) {
        this.essayMapper = essayMapper;
    }

    @Autowired
    public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }
}
