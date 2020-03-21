package com.yh_simon.community.controller;

import com.yh_simon.community.dto.FileDTO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class FileController {

    @PostMapping("/file/upload")
    @ResponseBody
    public FileDTO upload(){
        FileDTO fileDTO=new FileDTO();
        fileDTO.setSuccess(1);
        fileDTO.setMessage("上传成功");
        fileDTO.setUrl("/images/wechat.png");
        return fileDTO;
    }
}
