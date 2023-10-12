package com.yaxin.cms.bean.extend;

import com.yaxin.cms.bean.Role;
import com.yaxin.cms.bean.User;
import lombok.Data;

/**
 * @author shaoyb
 * @program: 230314-cms
 * @description TODO
 * @create 2023/3/14 17:34
 **/
@Data
public class UserExtend extends User {
    private Role role;
}
