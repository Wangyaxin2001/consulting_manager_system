package com.yaxin.cms.bean;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@TableName("cms_log")
public class Log implements Serializable {

    /**
    * 主键ID
    */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
    * 操作用户
    */
    private String username;
    /**
    * 接口描述信息
    */
    private String businessName;
    /**
    * 请求接口
    */
    private String requestUrl;
    /**
    * 请求方式
    */
    private String requestMethod;
    /**
    * ip
    */
    private String ip;
    /**
    * ip来源
    */
    private String source;
    /**
    * 请求接口耗时
    */
    private Long spendTime;
    /**
    * 创建时间
    */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private LocalDateTime createTime;
    /**
    * 请求参数
    */
    private String paramsJson;
    /**
    * 响应参数
    */
    private String resultJson;
}
