package com.zbw.blog.module.user;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zbw.blog.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper extends BaseMapper<User> {

    User login(@Param("email") String email, @Param("password") String password);

    User getUserByEmail(String email);

    User getUserByAccount(String account);

    int registerAccount(User user);

    int activateAccount(@Param("account") String account,@Param("email")String email);

    User findUserById(int id);

    int updateAvatar(@Param("filePath") String filePath,@Param("id") int id);

    int updateUser(User user);
}
