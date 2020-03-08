package com.yh_simon.community.controller;


import com.yh_simon.community.mapper.UserMapper;
import com.yh_simon.community.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Controller
public class IndexController {

    @Autowired
    private UserMapper userMapper;

    @GetMapping("/index")
    public String index(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie:cookies){
            if(cookie.getName().equals("token")){
                String token=cookie.getValue();
                User user=userMapper.findByToken(token);
//                System.out.println("通过查询数据库："+user);
                if(user!=null){
                    request.getSession().setAttribute("user", user);
                }
            }
        }
        return "index";
    }

    @GetMapping("/publish")
    public String publish(){
        return "publish";
    }
}
