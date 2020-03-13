package com.yh_simon.community.advice;

import com.yh_simon.community.exception.CustomizeException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@ControllerAdvice
public class CustomizeExceptionHandler{

    @ExceptionHandler(CustomizeException.class)
    ModelAndView handle(HttpServletRequest request, HttpServletResponse response,Throwable ex, Model model) {
        if(ex instanceof CustomizeException){
            model.addAttribute("message", ex.getMessage());
        }else{
            model.addAttribute("message","服务器冒烟了,要不然稍后再试试");
        }

        return new ModelAndView("error");
    }
}