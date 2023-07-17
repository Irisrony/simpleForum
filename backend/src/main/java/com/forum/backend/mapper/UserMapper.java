package com.forum.backend.mapper;

import com.forum.backend.domain.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface UserMapper {
    @Select("select * from t_user")
    List<User> findAll();

    @Insert("insert into t_user(username,password) values (#{username},#{password})")
    int insert(User user);

    int update(User user);

    List<User> findPage(Integer pageNum, Integer pageSize);

    Integer selectTotal();

    User selectUser(String username);
}
