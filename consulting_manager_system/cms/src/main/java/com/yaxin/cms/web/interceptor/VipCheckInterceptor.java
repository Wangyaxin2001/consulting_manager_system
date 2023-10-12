package com.yaxin.cms.web.interceptor;

import com.yaxin.cms.bean.User;
import com.yaxin.cms.dao.UserDao;
import com.yaxin.cms.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;

/**
 * @author shaoyb
 * @program: 230314-cms
 * @description TODO
 * @create 2023/3/20 12:46
 **/
//1.添加vip拦截器
@Component
public class VipCheckInterceptor implements HandlerInterceptor {

    @Autowired
    private UserDao userDao;

    // 拦截器前置处理方法
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //1.获取当前登录用户
        String token = request.getHeader("Authorization");
        //System.out.println("in preHandler token: " + token);
        Long userId = Long.parseLong(JwtUtil.getUserId(token));
        //System.out.println("in vip userId: " + userId);
        User user = userDao.selectById(userId);

        //2.如果当前用户是vip，过期判断及修改
        if(user.getIsVip() == 1) {
            int n = LocalDateTime.now().compareTo(user.getExpiresTime());
            //System.out.println("是否超时: " + n);
            if(n > 0) {
                //System.out.println("vip已过期");
                User u = new User();
                u.setId(userId);
                u.setIsVip(0);
                userDao.updateById(u);
            }
        }

        // 3.放行
        return true;
    }
}