package com.forum.backend.mapper;

import com.forum.backend.domain.Posts;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PostsMapper {
    int update(Posts posts);
    List<Posts> findPage(Integer pageNum,Integer pageSize);

    Integer selectTotal();

    Posts onePage(Integer id);
}
