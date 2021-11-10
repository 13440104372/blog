package com.zbw.blog.module.tag;

import com.zbw.blog.AppResponse;
import com.zbw.blog.pojo.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 17587
 */
@RestController
@RequestMapping("/tag")
public class TagController {

    private TagService tagService;

    @GetMapping("/findTagByKey")
    public AppResponse<List<Tag>> findTagByKey(@RequestParam("key") String key){
        return AppResponse.onSuccess(tagService.findTagByKey(key));
    }

    @PostMapping(value = "/findTagByKeyWithExcludes/{key}")
    public AppResponse<List<Tag>> findTagByKeyWithExcludes(@PathVariable("key") String key,
                                                           @RequestBody() List<Tag> tags){
        return AppResponse.onSuccess(tagService.findTagByKeyWithExcludes(key,tags));
    }

    @Autowired
    public void setTagService(TagService tagService) {
        this.tagService = tagService;
    }
}
