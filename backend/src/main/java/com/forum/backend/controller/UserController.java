package com.forum.backend.controller;

import com.forum.backend.domain.User;
import com.forum.backend.mapper.UserMapper;
import com.forum.backend.service.UserService;
import com.forum.backend.utils.CreateJwt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/")
    public List<User> findAll(){
        return userService.findAll();
    }

    @PostMapping("/register")
    public Map<String,Object> insertUser(@RequestBody User user){

        int status = userService.upload(user);
        boolean flag = status > 0;
        Map<String,Object> res = new HashMap<>();
        res.put("status",flag);
        return res;
    }

    @GetMapping("/page")
    public Map<String,Object> findPage(@RequestParam Integer pageNum,@RequestParam Integer pageSize){
        pageNum = (pageNum - 1) * pageSize;
        List<User> data = userService.findPage(pageNum,pageSize);
        Integer total = userService.selectTotal();
        Map<String,Object> res = new HashMap<>();
        res.put("data",data);
        res.put("total",total);
        return res;
    }

    @PostMapping("/login")
    public Map<String,Object> validateUser(@RequestBody User user){
        User u = userService.selectUser(user.getUsername());
        String token = null;
        boolean flag = false;
        if (u != null && u.getPassword().equals(user.getPassword())){
            flag = true;
            token = CreateJwt.getToken(u);
        }
        Map<String,Object> res = new HashMap<>();
        res.put("token",token);
        res.put("status",flag);
        return res;
    }
}
