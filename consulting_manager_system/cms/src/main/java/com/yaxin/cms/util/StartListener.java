package com.yaxin.cms.util;

import com.yaxin.cms.bean.Article;
import com.yaxin.cms.bean.User;
import com.yaxin.cms.dao.ArticleDao;
import com.yaxin.cms.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StartListener implements ApplicationListener<ApplicationReadyEvent> {

    @Autowired
    private ArticleDao articleDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private RedisUtil redisUtil;
    private final String REDIS_KEY = "Article_Read_Num";

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        System.out.println("应用程序已启动！");
        List<Article> records = articleDao.selectList(null);
        for (Article art : records) {
            Long userId = art.getUserId();
            User user = userDao.queryUserById(userId);
            //如果用户已经被删除，则文章不可见
            if (user == null)
                continue;
            //从redis中获取浏览量 更新对象属性值
            //1.先查看redis中是否存在文章浏览量，如果没有则写入
            Object obj = redisUtil.getHash(REDIS_KEY)
                    .get(art.getId().toString());
            if(obj == null) {
                redisUtil.hset(REDIS_KEY, art.getId().toString(), art.getReadNum());
            }
        }
        System.out.println("Redis读量初始化完毕");
    }
}
