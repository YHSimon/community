package com.yh_simon.community.controller;


import com.yh_simon.community.dto.CommentDTO;
import com.yh_simon.community.dto.QuestionDTO;
import com.yh_simon.community.enums.CommentTypeEnum;
import com.yh_simon.community.model.Question;
import com.yh_simon.community.service.CommentService;
import com.yh_simon.community.service.QuestionService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private CommentService commentService;

    @GetMapping("/question/{id}")
    public String question(@PathVariable("id") Long id, Model model){
        QuestionDTO questionDTO=questionService.getById(id);
        List<CommentDTO> comments=commentService.listByTargetId(id, CommentTypeEnum.QUESTION);
        String regexp=questionDTO.getTag().replace(",", "|");
        List<Question> relatedQuestions=questionService.findRelatedQuestionsByTag(regexp, id);
        questionService.incView(id);
        model.addAttribute("question", questionDTO);
        model.addAttribute("comments", comments);
        model.addAttribute("relatedQuestions", relatedQuestions);
        return "question";
    }


}
