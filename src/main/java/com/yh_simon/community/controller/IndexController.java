package com.yh_simon.community.controller;


import com.yh_simon.community.dto.PaginationDTO;
import com.yh_simon.community.dto.QuestionDTO;
import com.yh_simon.community.mapper.UserMapper;
import com.yh_simon.community.model.User;
import com.yh_simon.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Controller
public class IndexController {

    @Autowired
    private UserMapper userMapper;


    @Autowired
    private QuestionService questionService;

    @GetMapping("/")
    public String index(HttpServletRequest request, Model model, @RequestParam(value = "page", defaultValue = "1") Integer page, @RequestParam(value = "limit", defaultValue = "3") Integer limit) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length != 0) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")) {
                    String token = cookie.getValue();
                    User user = userMapper.findByToken(token);
                    if (user != null) {
                        request.getSession().setAttribute("user", user);
                    }
                }
            }
        }
        PaginationDTO paginationDTO = questionService.showQuestions(page, limit);
        model.addAttribute("pagination", paginationDTO);
        return "index";
    }
}
