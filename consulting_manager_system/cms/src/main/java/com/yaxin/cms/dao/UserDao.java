package com.yaxin.cms.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yaxin.cms.bean.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yaxin.cms.bean.extend.UserExtend;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author briup
 * @since 2023-03-10
 */
public interface UserDao extends BaseMapper<User> {

    //分页+条件查询用户(含角色)
    IPage<UserExtend> queryAllUserWithRole(IPage<UserExtend> page,
                                           @Param("username") String username,
                                           @Param("status") String status,
                                           @Param("roleId") Integer roleId,
                                           @Param("isVip") Integer isVip);

    User queryUserById(@Param("id") Long id);
}
