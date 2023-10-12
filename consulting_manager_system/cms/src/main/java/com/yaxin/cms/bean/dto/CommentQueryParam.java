package com.yaxin.cms.bean.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author shaoyb
 * @program: 230314-cms
 * @description TODO
 * @create 2023/3/22 15:25
 **/
@Data
public class CommentQueryParam {
    // 分页参数
    private Integer pageNum;
    private Integer pageSize;

    private Long userId;
    private Long articleId;
    // 关键字
    private String keyword;
    // 发表时间范围
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private LocalDateTime startTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private LocalDateTime endTime;
}
