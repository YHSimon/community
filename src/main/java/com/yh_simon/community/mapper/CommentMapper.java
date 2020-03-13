package com.yh_simon.community.mapper;

import com.yh_simon.community.model.Comment;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CommentMapper {

    @Insert("insert into comment(parent_id,type,content,commentator,gmt_create,gmt_modified,like_count) values(#{parentId},#{type},#{content},#{commentator},#{gmtCreate},#{gmtModified},#{likeCount})")
    void insert(Comment comment);
}
