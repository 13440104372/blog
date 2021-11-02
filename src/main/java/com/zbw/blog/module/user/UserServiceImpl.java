package com.zbw.blog.module.user;


import com.zbw.blog.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("userService")
public class UserServiceImpl implements UserService{

    private UserMapper userMapper;

    @Override
    public User login(String email, String password) {
        return userMapper.login(email,password);
    }

    @Override
    public User getUserByEmail(String email) {
        return userMapper.getUserByEmail(email);
    }

    @Override
    public User getUserByAccount(String account) {
        return userMapper.getUserByAccount(account);
    }

    @Override
    public boolean registerAccount(User user) {
        return userMapper.registerAccount(user)>0;
    }

    @Override
    public boolean activateAccount(String account, String email) {
        return userMapper.activateAccount(account, email)>0;
    }

    @Override
    public User findUserById(int id) {
        return userMapper.findUserById(id);
    }

    @Override
    public boolean updateAvatar(String filePath, int id) {
        return userMapper.updateAvatar(filePath,id)>0;
    }

    @Override
    public boolean updateUser(User user) {
        return userMapper.updateUser(user)>0;
    }


    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }
}
