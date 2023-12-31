package com.yaxin.cms.bean.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author shaoyb
 * @program: 230314-cms
 * @description TODO
 * @create 2023/3/22 17:31
 **/
@Data
public class LogParam {
    private Integer pageNum;
    private Integer pageSize;

    //发送请求的用户
    private String username;
    //请求的url
    private String requestUrl;

    //日志时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private LocalDateTime startTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private LocalDateTime endTime;
}
