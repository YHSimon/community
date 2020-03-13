package com.yh_simon.community.model;

import lombok.Data;

@Data
public class Comment {
    private Long id;
    private Long parentId;
    private Integer type;
    private String content;
    private Long commentator;
    private Long gmtCreate;
    private Long gmtModified;
    private Long likeCount;
    private Integer commentCount;
}
