package com.yh_simon.community.dto;

import lombok.Data;

@Data
public class NotificationDTO {
    private Long id;
    private Long gmtCreate;
    private Integer status;
    private Long notifier;
    private String notifiername;
    private Long outerid;
    private String outertitle;
    private Integer type;
    private String typeName;
}
