package com.zbw.blog.module.user;


import com.zbw.blog.pojo.User;

public interface UserService {
    User login(String email, String password);

    User getUserByEmail(String email);

    User getUserByAccount(String account);

    boolean registerAccount(User user);

    boolean activateAccount(String account,String email);

    User findUserById(int id);

    boolean updateAvatar(String filePath,int id);

    boolean updateUser(User user);
}
