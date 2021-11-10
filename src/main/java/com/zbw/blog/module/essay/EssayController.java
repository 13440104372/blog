package com.zbw.blog.module.essay;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zbw.blog.AppResponse;
import com.zbw.blog.pojo.Essay;
import com.zbw.blog.security.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 * @author 17587
 */
@RestController
@RequestMapping("/essay")
public class EssayController {

    private EssayService essayService;


    /**
     * 分页查找已发布的文章
     * @param page 页码
     * @param size 数据量
     * @param authentication 认证信息
     * @return 文章页
     */
    @GetMapping("/findReleasedEssayByUserWithPage")
    public AppResponse<IPage<Essay>> findReleasedEssayByUserWithPage(@RequestParam("page") Long page,
                                                                     @RequestParam("size") Long size,
                                                                     Authentication authentication) {
        LoginUser loginUser = (LoginUser) authentication.getDetails();
        return AppResponse.onSuccess(essayService.findReleasedEssayByUserIdWithPage(loginUser.getUser().getId(), page, size));
    }

    /**
     * 分页查找未发布的文章
     * @param page 页码
     * @param size 数据量
     * @param authentication 认证信息
     * @return 文章页
     */
    @GetMapping("/findUnReleasedEssayByUserWithPage")
    public AppResponse<IPage<Essay>> findUnReleasedEssayByUserWithPage(@RequestParam("page") Long page,
                                                                      @RequestParam("size") Long size,
                                                                      Authentication authentication) {
        LoginUser loginUser = (LoginUser) authentication.getDetails();
        return AppResponse.onSuccess(essayService.findUnReleasedEssayByUserIdWithPage(loginUser.getUser().getId(),page,size));
    }

    /**
     * 分页查找已删除的文章
     * @param page 页码
     * @param size 数据量
     * @param authentication 认证信息
     * @return 文章页
     */
    @GetMapping("/findDeletedEssayByUserWithPage")
    public AppResponse<IPage<Essay>> findDeletedEssayByUserWithPage(@RequestParam("page") Long page,
                                                                       @RequestParam("size") Long size,
                                                                       Authentication authentication) {
        LoginUser loginUser = (LoginUser) authentication.getDetails();
        return AppResponse.onSuccess(essayService.findDeletedEssayByUserIdWithPage(loginUser.getUser().getId(),page,size));
    }

    /**
     * 发布文章
     * @param essay 文章
     * @param authentication 认证信息
     * @return 文章id
     */
    @PostMapping("/releaseEssay")
    public AppResponse<Long> releaseEssay(@RequestBody Essay essay,Authentication authentication) {
        LoginUser loginUser = (LoginUser) authentication.getDetails();
        essay.setUser(loginUser.getUser());
        if(essayService.releaseEssay(essay)){
            return AppResponse.onSuccess(essay.getId());
        }
        return AppResponse.onError(-1,"发布失败");
    }

    /**
     * 存草稿
     * @param essay 文章
     * @param authentication 认证信息
     * @return 文章id
     */
    @PostMapping("/preRelease")
    public AppResponse<Long> preRelease(@RequestBody Essay essay,Authentication authentication) {
        LoginUser loginUser = (LoginUser) authentication.getDetails();
        essay.setUser(loginUser.getUser());
        if(essayService.preRelease(essay)){
            return AppResponse.onSuccess(essay.getId());
        }
        return AppResponse.onError(-1,"保存失败");
    }

    /**
     * 删除文章（将删除标记设为1）
     * @param essay 文章
     * @return 成功返回true
     */
    @PostMapping("/deleteEssay")
    public AppResponse<Boolean> deleteEssay(@RequestBody Essay essay){
        return AppResponse.onSuccess(essayService.deleteEssay(essay));
    }

    /**
     * 恢复文章（将删除标记设为0）
     * @param essay 文章
     * @return 成功返回true
     */
    @PostMapping("/recoverEssay")
    public AppResponse<Boolean> recoverEssay(@RequestBody Essay essay){
        return AppResponse.onSuccess(essayService.recoverEssay(essay));
    }

    /**
     * 移除文章（彻底删除）
     * @param essay 文章
     * @return 成功返回true
     */
    @PostMapping("/removeEssay")
    public AppResponse<Boolean> removeEssay(@RequestBody Essay essay){
        return AppResponse.onSuccess(essayService.removeEssay(essay));
    }

    /**
     * 删除单个标签
     * @param essayId 文章id
     * @param tagId 标签id
     * @return 删除成功返回true
     */
    @GetMapping("/deleteEssayTagRelation")
    public AppResponse<Boolean> deleteEssayTagRelation(@RequestParam("essayId") Long essayId,
                                                       @RequestParam("tagId") Long tagId){
        return AppResponse.onSuccess(essayService.deleteEssayTagRelation(essayId,tagId));
    }

    @Autowired
    public void setEssayService(EssayService essayService) {
        this.essayService = essayService;
    }
}
