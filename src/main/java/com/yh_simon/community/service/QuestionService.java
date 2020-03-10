package com.yh_simon.community.service;


import com.yh_simon.community.dto.QuestionDTO;
import com.yh_simon.community.mapper.QuestionMapper;
import com.yh_simon.community.mapper.UserMapper;
import com.yh_simon.community.model.Question;
import com.yh_simon.community.model.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private QuestionMapper questionMapper;

    public List<QuestionDTO> showQuestions(){
        List<Question> questions=questionMapper.findAllQuestions();
        List<QuestionDTO> questionDTOS=new ArrayList<>();
        for (Question question:questions){
            QuestionDTO questionDTO=new QuestionDTO();
            BeanUtils.copyProperties(question, questionDTO);
            User user=userMapper.findById(question.getCreator());
            questionDTO.setUser(user);
            questionDTOS.add(questionDTO);
        }
        return questionDTOS;
    }

}
