package com.yaxin.cms.bean.dto;

import lombok.Data;

/**
 * @author shaoyb
 * @program: 230314-cms
 * @description TODO
 * @create 2023/3/22 15:19
 **/
@Data
public class CommentDeleteParam {
    // 评论类型，1：一级  2：二级
    private Integer type;

    // 评论id
    private Long id;
}
