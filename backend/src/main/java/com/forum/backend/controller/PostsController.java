package com.forum.backend.controller;

import com.forum.backend.domain.Posts;
import com.forum.backend.service.PostsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/posts")
public class PostsController {
    @Autowired
    private PostsService postsService;

    @GetMapping("/page")
    public Map<String,Object> findPage(@RequestParam Integer pageNum,@RequestParam Integer pageSize){
        pageNum = (pageNum - 1) * pageSize;
        List<Posts> data = postsService.findPage(pageNum,pageSize);
        Integer total = postsService.selectTotal();
        Map<String,Object> res = new HashMap<>();
        res.put("data",data);
        res.put("total",total);
        return res;
    }

    @PostMapping("/upload")
    public Map<String,Object> update(@RequestBody Posts posts){
        int status = postsService.update(posts);
        boolean flag = status > 0;
        Map<String,Object> res = new HashMap<>();
        res.put("status",flag);
        return res;
    }

    @GetMapping("/onePage")
    public Map<String,Object> onePage(@RequestParam Integer id){
        Posts posts = postsService.onePage(id);
        boolean flag = posts != null;
        Map<String,Object> res = new HashMap<>();
        res.put("data",posts);
        res.put("status",flag);
        System.out.println(res);
        return res;
    }
}
