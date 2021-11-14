package com.zbw.blog.module.user;

import com.zbw.blog.AppResponse;
import com.zbw.blog.module.file.FileService;
import com.zbw.blog.pojo.User;
import com.zbw.blog.security.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

import static com.zbw.blog.AppResponseCode.NEED_LOGIN;

/**
 * 用户
 *
 * @author 17587
 */
@RestController
@RequestMapping("/user")
public class UserController {

    private UserService userService;

    private SessionRegistry sessionRegistry;

    private FileService fileService;



    /**
     * 获取已认证通过的个人信息
     *
     * @param authentication 认证信息
     * @return 用户信息
     */
    @PostMapping("/getUserInfo")
    public AppResponse<User> getUserInfo(Authentication authentication) {
        LoginUser loginUser = (LoginUser) authentication.getDetails();
        return AppResponse.onSuccess(loginUser.getUser());
    }

    /**
     * 获取系统在线用户
     */
    @GetMapping("/getOnlineUser")
    @PreAuthorize("hasRole('master')")
    public AppResponse<List<Object>> getOnlineUser() {
        List<Object> list = sessionRegistry.getAllPrincipals();
        return AppResponse.onSuccess(list);
    }


    /**
     * 更新头像
     * @param avatarFile 头像文件
     * @param authentication 认证信息
     * @return 更新成功返回头像地址
     * @throws IOException 上传文件时的异常
     */
    @PostMapping("/updateAvatar")
    public AppResponse<String> updateAvatar(@RequestParam("avatar") MultipartFile avatarFile, Authentication authentication) throws IOException {
        if (avatarFile.isEmpty()) {
            return AppResponse.onError(-1, "文件不能为空");
        }
        LoginUser loginUser = (LoginUser) authentication.getDetails();
        User user = loginUser.getUser();
        String result = fileService.uploadFile(avatarFile, "/avatars");
        if (result != null && !result.isEmpty()) {
            if (userService.updateAvatar(result, user.getId())) {
                return AppResponse.onSuccess(result);
            }
            else {
                return AppResponse.onError(-1, "保存到数据库失败");
            }
        }
        return AppResponse.onError(-1, "上传失败");
    }

    @PostMapping("/updateUser")
    public AppResponse<Boolean> updateUser(@RequestBody User user) {
        return AppResponse.onSuccess(userService.updateUser(user));
    }

    @PostMapping("/login")
    public AppResponse<Boolean> login(Authentication authentication){
        if(null != authentication.getDetails()){
            return AppResponse.onSuccess(true);
        }
        else {
            return AppResponse.onError(NEED_LOGIN,"认证失败！");
        }
    }

    private boolean isValidEmail(String email) {
        if ((email != null) && (!email.isEmpty())) {
            return Pattern.matches("^(\\w+([-.][A-Za-z0-9]+)*){3,18}@\\w+([-.][A-Za-z0-9]+)*\\.\\w+([-.][A-Za-z0-9]+)*$", email);
        }
        return false;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setSessionRegistry(SessionRegistry sessionRegistry) {
        this.sessionRegistry = sessionRegistry;
    }

    @Autowired
    public void setFileService(FileService fileService) {
        this.fileService = fileService;
    }
}
