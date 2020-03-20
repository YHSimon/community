package com.yh_simon.community.controller;


import com.yh_simon.community.dto.NotificationDTO;
import com.yh_simon.community.enums.NotificationTypeEnum;
import com.yh_simon.community.model.Notification;
import com.yh_simon.community.model.User;
import com.yh_simon.community.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;

@Controller
public class NotificationController {
    @Autowired
    private NotificationService notificationService;

    @GetMapping("/notification/{id}")
    public String profile(@PathVariable("id") Long id, HttpServletRequest request){
        User user=(User) request.getSession().getAttribute("user");
        if(user==null){
            return "redirect:/";
        }
        NotificationDTO notificationDTO=notificationService.read(id, user);
        if(NotificationTypeEnum.REPLY_COMMENT.getType()==notificationDTO.getType()||NotificationTypeEnum.REPLY_QUESTION.getType()==notificationDTO.getType()){
            return "redirect:/question/"+notificationDTO.getOuterid();
        }else {
            return "redirect:/";
        }
    }
}
