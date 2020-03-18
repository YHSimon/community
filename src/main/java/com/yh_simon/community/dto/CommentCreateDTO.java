package com.yh_simon.community.dto;

import lombok.Data;

@Data
public class CommentCreateDTO {
    private Long parentId;
    private Integer type;
    private String content;
}
