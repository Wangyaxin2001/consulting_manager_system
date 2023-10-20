package com.yaxin.cms.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author briup
 * @since 2023-03-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("cms_comment")
public class Comment implements Serializable {

    private static final long serialVersionUID = 1L;

    //评论id
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    //评论内容
    private String content;

    //发布时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private LocalDateTime publishTime;

    //用户id
    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId;

    //文章id
    @JsonSerialize(using = ToStringSerializer.class)
    private Long articleId;

    //评论删除状态
    @TableLogic
    private Integer deleted;


}
