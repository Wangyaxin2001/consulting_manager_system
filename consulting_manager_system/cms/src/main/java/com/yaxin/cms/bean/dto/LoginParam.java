package com.yaxin.cms.bean.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author shaoyb
 * @program: 230314-cms
 * @description TODO
 * @create 2023/3/15 14:54
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginParam {
    private String username;
    private String password;
}
