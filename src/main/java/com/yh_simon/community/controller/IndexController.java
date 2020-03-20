package com.yh_simon.community.controller;


import com.yh_simon.community.dto.PaginationDTO;
import com.yh_simon.community.mapper.UserMapper;
import com.yh_simon.community.model.User;
import com.yh_simon.community.model.UserExample;
import com.yh_simon.community.service.NotificationService;
import com.yh_simon.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;


@Controller
public class IndexController {


    @Autowired
    private QuestionService questionService;

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private NotificationService notificationService;

    @GetMapping("/")
    public String index(HttpServletRequest request,Model model, @RequestParam(value = "page", defaultValue = "1") Integer page, @RequestParam(value = "limit", defaultValue = "3") Integer limit) {
        PaginationDTO paginationDTO = questionService.showQuestions(page, limit);
        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length != 0) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")) {
                    String token = cookie.getValue();
                    UserExample example = new UserExample();
                    example.createCriteria()
                            .andTokenEqualTo(token);
                    List<User> users = userMapper.selectByExample(example);
                    if (users.size() != 0) {
                        User user = users.get(0);
                        request.getSession().setAttribute("user", user);
                        request.getSession().setAttribute("unreadCount", notificationService.unreadCount(user.getId()));
                    }
                    break;
                }
            }
        }
        model.addAttribute("pagination", paginationDTO);

        return "index";
    }
}
