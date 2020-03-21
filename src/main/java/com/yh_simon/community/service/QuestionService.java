package com.yh_simon.community.service;


import com.yh_simon.community.dto.PaginationDTO;
import com.yh_simon.community.dto.QuestionDTO;
import com.yh_simon.community.exception.CustomizeErrorCode;
import com.yh_simon.community.exception.CustomizeException;
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
            User user = userMapper.selectByPrimaryKey(question.getCreator());
            questionDTO.setUser(user);
            questionDTOS.add(questionDTO);
        }
        paginationDTO.setData(questionDTOS);
        return paginationDTO;
    }

    public PaginationDTO findQuestionsByUserId(Long id, Integer page, Integer limit) {
        PaginationDTO paginationDTO = new PaginationDTO();
        int totalCount = questionMapper.countByUserId(id);
//        System.out.println(totalCount);
        if (totalCount != 0) {
            paginationDTO.setPagination(totalCount, page, limit);
            int index = (paginationDTO.getPage() - 1) * limit;
            List<Question> questions = questionMapper.findQuestionsByUserId(id, index, limit);
            List<QuestionDTO> questionDTOS = new ArrayList<>();
            for (Question question : questions) {
                QuestionDTO questionDTO = new QuestionDTO();
                BeanUtils.copyProperties(question, questionDTO);
                User user = userMapper.selectByPrimaryKey(question.getCreator());
                questionDTO.setUser(user);
                questionDTOS.add(questionDTO);
            }
            paginationDTO.setData(questionDTOS);
        }
        return paginationDTO;

    }

    public QuestionDTO getById(Long id) {
        QuestionDTO questionDTO=new QuestionDTO();
        Question question=questionMapper.selectByPrimaryKey(id);
        if(question==null){
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUNT);
        }
        BeanUtils.copyProperties(question, questionDTO);
        User user = userMapper.selectByPrimaryKey(question.getCreator());
        questionDTO.setUser(user);
        return questionDTO;
    }

    public List<Question>  findRelatedQuestionsByTag(String regexp,Long id){
        return questionMapper.findRelatedQuestionsByTag(regexp, id);
    }

    public void createOrUpdate(Question question) {
        if(question.getId()==null){
            int i = questionMapper.insertSelective(question);
            if(i!=1)
            {
                throw new CustomizeException(CustomizeErrorCode.INVALID_INPUT);
            }
        }else{
            question.setGmtModified(System.currentTimeMillis());
            int updated = questionMapper.updateByPrimaryKeySelective(question);
            if(updated!=1)
            {
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUNT);
            }
        }
    }

    public void incView(Long id) {
         questionMapper.incView(id);
    }

    public PaginationDTO search(String searchText, Integer page, Integer limit) {
        PaginationDTO paginationDTO = new PaginationDTO();
        int totalCount = questionMapper.countBySearchText(searchText);
        if(totalCount==0){
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUNT);
        }
        paginationDTO.setPagination(totalCount, page, limit);
        int index = (paginationDTO.getPage() - 1) * limit;
        List<Question> questions = questionMapper.search(searchText,index, limit);
        List<QuestionDTO> questionDTOS = new ArrayList<>();
        for (Question question : questions) {
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question, questionDTO);
            User user = userMapper.selectByPrimaryKey(question.getCreator());
            questionDTO.setUser(user);
            questionDTOS.add(questionDTO);
        }
        paginationDTO.setData(questionDTOS);
        return paginationDTO;
    }
}
