package com.zbw.blog.module.open;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zbw.blog.AppResponse;
import com.zbw.blog.module.essay.EssayService;
import com.zbw.blog.pojo.Essay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.zbw.blog.AppResponseCode.NOT_FOUND;

/**
 * 有关文章的开放接口
 * @author 17587
 */
@RestController
@RequestMapping("/open/essay")
public class OpenedEssayController {

    private EssayService essayService;

    @GetMapping("/findReleasedEssayByUserWithPage")
    public AppResponse<IPage<Essay>> findReleasedEssayByUserWithPage(@RequestParam("userId") Long userId,
                                                                     @RequestParam("page") Long page,
                                                                     @RequestParam("size") Long size){
        return AppResponse.onSuccess(essayService.findReleasedEssayByUserIdWithPage(userId, page, size));
    }

    @GetMapping("/findReleasedEssayById")
    public AppResponse<Essay> findReleasedEssayById(@RequestParam("essayId") Long essayId){
        Essay essay = essayService.findReleasedEssayById(essayId);
        if(essay == null){
            return AppResponse.onError(NOT_FOUND,"文章不存在！");
        }
        return AppResponse.onSuccess(essay);
    }

    @Autowired
    public void setEssayService(EssayService essayService) {
        this.essayService = essayService;
    }
}
