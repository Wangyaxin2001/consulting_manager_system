package com.yaxin.cms.config;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yaxin.cms.bean.User;
import com.yaxin.cms.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author shaoyb
 * @program: 230314-cms
 * @description 定时器任务类，每隔一段时间判断cms_user表中user是否vip过期
 * @create 2023/3/20 15:53
 **/
@Component
public class VipTimerTask {
    @Autowired
    private UserDao userDao;

    // 每20分钟运行1次
    @Scheduled(cron = "0 0/20 * * * ?")
    public void vipExpirationCheck() {
        System.out.println("in VipTimerTask ..." + LocalDateTime.now());

        // 1.获取所有的vip用户
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getIsVip, 1);
        List<User> list = userDao.selectList(wrapper);
        for (User user : list) {
            // 2.判断vip是否过期
            if(LocalDateTime.now().compareTo(user.getExpiresTime()) > 0) {
                // 3.修改过期用户的isVip值
                User u = new User();
                u.setId(user.getId());
                u.setIsVip(0);
                userDao.updateById(u);
            }
        }
    }
}
