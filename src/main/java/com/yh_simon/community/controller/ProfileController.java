package com.yh_simon.community.controller;


import com.yh_simon.community.dto.PaginationDTO;
import com.yh_simon.community.mapper.UserMapper;
import com.yh_simon.community.model.User;
import com.yh_simon.community.service.NotificationService;
import com.yh_simon.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ProfileController {


    @Autowired
    UserMapper userMapper;
    @Autowired
    QuestionService questionService;
    @Autowired
    NotificationService notificationService;

    @GetMapping("/profile/{action}")
    public String profile(@PathVariable("action") String action, Model model, HttpServletRequest request, @RequestParam(value = "page",defaultValue = "1") Integer page,@RequestParam(value = "limit",defaultValue = "3") Integer limit){

        User user=(User)request.getSession().getAttribute("user");

        if(user==null){
            return "redirect:/";
        }
        if("questions".equals(action)){
            PaginationDTO paginationDTO=questionService.findQuestionsByUserId(user.getId(),page,limit);
            model.addAttribute("section", "questions");
            model.addAttribute("sectionName", "我的问题");
            model.addAttribute("pagination", paginationDTO);
        }else  if ("replies".equals(action)){
            PaginationDTO paginationDTO=notificationService.findNotifications(user.getId(),page,limit);
            model.addAttribute("section", "replies");
            model.addAttribute("sectionName", "最新回复");
            model.addAttribute("pagination", paginationDTO);
        }
        return "profile";
    }
}
