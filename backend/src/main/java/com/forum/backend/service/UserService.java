package com.forum.backend.service;

import com.forum.backend.domain.User;
import com.forum.backend.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    public int upload(User user){
        if (user.getId() == null){
            return userMapper.insert(user);
        }else{
            return userMapper.update(user);
        }
    }

    public List<User> findAll(){
        return userMapper.findAll();
    }

    public List<User> findPage(Integer pageNum,Integer pageSize) {
        return userMapper.findPage(pageNum,pageSize);
    }

    public Integer selectTotal() {
        return userMapper.selectTotal();
    }

    public User selectUser(String username) {
        return userMapper.selectUser(username);
    }
}
