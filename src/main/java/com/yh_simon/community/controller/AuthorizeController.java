package com.yh_simon.community.controller;

import com.yh_simon.community.dto.AccesstokenDTO;
import com.yh_simon.community.dto.GithubUser;
import com.yh_simon.community.mapper.UserMapper;
import com.yh_simon.community.model.User;
import com.yh_simon.community.provider.GithubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Controller
public class AuthorizeController {

    @Autowired
    private GithubProvider githubProvider;
    @Autowired
    private UserMapper userMapper;

    @Value("${github.redirect.uri}")
    private String redirectUri;
    @Value("${github.client.id}")
    private String clientId;
    @Value("${github.client.secret}")
    private String clientSecret;




    @GetMapping("/callback")
    public String callback(@RequestParam("code") String code, @RequestParam("state") String state, HttpServletResponse response){
        AccesstokenDTO accesstokenDTO = new AccesstokenDTO();
        accesstokenDTO.setCode(code);
        accesstokenDTO.setRedirect_uri(redirectUri);
        accesstokenDTO.setClient_id(clientId);
        accesstokenDTO.setState(state);
        accesstokenDTO.setClient_secret(clientSecret);
        String accessToken = githubProvider.getAccesstoken(accesstokenDTO);
        GithubUser githubUser = githubProvider.getUser(accessToken);
//        System.out.println(user.getLogin());
        if(githubUser!=null&&githubUser.getId()!=null){
            User user = new User();
            user.setAccountId(String.valueOf(githubUser.getId()));
            user.setName(githubUser.getName());
            String token=UUID.randomUUID().toString();
            user.setToken(token);
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            user.setAvatarUrl(githubUser.getAvatarUrl());
            userMapper.insert(user);
            Cookie cookie = new Cookie("token", token);
            response.addCookie(cookie);
//            request.getSession().setAttribute("user", user);
            return "redirect:/";
        }
        else{
            return "redirect:/";
        }
    }
}
