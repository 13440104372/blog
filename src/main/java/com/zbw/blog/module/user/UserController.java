package com.zbw.blog.module.user;

import com.zbw.blog.AppResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @GetMapping("/test")
    public AppResponse<String> test(){
        return AppResponse.onSuccess("success");
    }
}
