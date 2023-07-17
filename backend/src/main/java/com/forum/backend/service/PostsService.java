package com.forum.backend.service;

import com.forum.backend.domain.Posts;
import com.forum.backend.mapper.PostsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostsService {
    @Autowired
    private PostsMapper postsMapper;

    public int update(Posts posts){
        return postsMapper.update(posts);
    }

    public List<Posts> findPage(Integer pageNum,Integer pageSize){
        return postsMapper.findPage(pageNum,pageSize);
    }

    public Integer selectTotal(){
        return postsMapper.selectTotal();
    }

    public Posts onePage(Integer id){
        return postsMapper.onePage(id);
    }

}
