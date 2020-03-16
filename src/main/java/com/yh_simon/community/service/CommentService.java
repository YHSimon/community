package com.yh_simon.community.service;

import com.yh_simon.community.dto.CommentDTO;
import com.yh_simon.community.enums.CommentTypeEnum;
import com.yh_simon.community.exception.CustomizeErrorCode;
import com.yh_simon.community.exception.CustomizeException;
import com.yh_simon.community.mapper.CommentMapper;
import com.yh_simon.community.mapper.QuestionMapper;
import com.yh_simon.community.mapper.UserMapper;
import com.yh_simon.community.model.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CommentService {

    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private UserMapper userMapper;


    @Transactional
    public void insert(Comment comment) {
        if(comment.getParentId()==null||comment.getParentId()==0){
            throw new CustomizeException(CustomizeErrorCode.TARGET_PARAM_NOT_FOUND);
        }
        if(comment.getType()==null|| !CommentTypeEnum.isExist(comment.getType())){
            throw new CustomizeException(CustomizeErrorCode.TYPE_PARAM_WRONG);
        }
        if(comment.getType()==CommentTypeEnum.COMMENT.getType()){
            //回复评论

        }else{
            //回复问题
            Question question=questionMapper.selectByPrimaryKey(comment.getParentId());
            if(question==null){
                throw new CustomizeException(CustomizeErrorCode.COMMENT_NOT_FOUND);
            }
            commentMapper.insertSelective(comment);
            questionMapper.incCommentCount(question);
        }
    }

    public List<CommentDTO> listByQustionId(Long id) {
        CommentExample commentExample = new CommentExample();
        commentExample.setOrderByClause("gmt_create desc");
        commentExample.createCriteria().
                andParentIdEqualTo(id).
                andTypeEqualTo(CommentTypeEnum.QUESTION.getType());
        List<Comment> comments = commentMapper.selectByExample(commentExample);
        if(comments.size()==0){
            return null;
        }

        //获取去重的评论人
        Set<Long> commentators = comments.stream().map(comment -> comment.getCommentator()).collect(Collectors.toSet());
        List<Long> userIds=new ArrayList<>();
        userIds.addAll(commentators);

        //获取评论人并转换为Map
        UserExample userExample = new UserExample();
        userExample.createCriteria().
                andIdIn(userIds);
        List<User> users = userMapper.selectByExample(userExample);
        Map<Long, User> userMap = users.stream().collect(Collectors.toMap(user -> user.getId(), user -> user));

        //将comment 转换为 commentDTO
        List<CommentDTO> commentDTOS = comments.stream().map(comment -> {
            CommentDTO commentDTO = new CommentDTO();
            BeanUtils.copyProperties(comment, commentDTO);
            commentDTO.setUser(userMap.get(comment.getCommentator()));
            return commentDTO;
        }).collect(Collectors.toList());
        return commentDTOS;
    }
}
