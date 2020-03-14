package com.yh_simon.community.service;

import com.yh_simon.community.enums.CommentTypeEnum;
import com.yh_simon.community.exception.CustomizeErrorCode;
import com.yh_simon.community.exception.CustomizeException;
import com.yh_simon.community.mapper.CommentMapper;
import com.yh_simon.community.mapper.QuestionMapper;
import com.yh_simon.community.model.Comment;
import com.yh_simon.community.model.Question;
import com.yh_simon.community.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentService {

    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private CommentMapper commentMapper;

    public void insert(Comment comment, User commentator) {
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
            comment.setCommentCount(0);
            commentMapper.insert(comment);
            questionMapper.incCommentCount(question);
        }
    }
}
