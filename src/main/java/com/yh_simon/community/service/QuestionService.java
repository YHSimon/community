package com.yh_simon.community.service;


import com.yh_simon.community.dto.PaginationDTO;
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

    public PaginationDTO showQuestions(Integer page, Integer limit) {
        PaginationDTO paginationDTO = new PaginationDTO();
        int totalCount = questionMapper.count();
        paginationDTO.setPagination(totalCount, page, limit);
        int index = (paginationDTO.getPage() - 1) * limit;
        List<Question> questions = questionMapper.findAllQuestions(index, limit);
        List<QuestionDTO> questionDTOS = new ArrayList<>();
        for (Question question : questions) {
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question, questionDTO);
            User user = userMapper.findById(question.getCreator());
            questionDTO.setUser(user);
            questionDTOS.add(questionDTO);
        }
        paginationDTO.setQuestions(questionDTOS);
        return paginationDTO;
    }

    public PaginationDTO findQuestionsByUserId(Integer id, Integer page, Integer limit) {
        PaginationDTO paginationDTO = new PaginationDTO();
        int totalCount = questionMapper.countByUserId(id);
        System.out.println(totalCount);
        if (totalCount != 0) {
            paginationDTO.setPagination(totalCount, page, limit);
            int index = (paginationDTO.getPage() - 1) * limit;
            List<Question> questions = questionMapper.findAllQuestionsByUserId(id, index, limit);
            List<QuestionDTO> questionDTOS = new ArrayList<>();
            for (Question question : questions) {
                QuestionDTO questionDTO = new QuestionDTO();
                BeanUtils.copyProperties(question, questionDTO);
                User user = userMapper.findById(question.getCreator());
                questionDTO.setUser(user);
                questionDTOS.add(questionDTO);
            }
            paginationDTO.setQuestions(questionDTOS);
        }
        return paginationDTO;

    }
}
