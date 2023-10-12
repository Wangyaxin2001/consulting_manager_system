package com.yaxin.cms.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author shaoyb
 * @program: 230308-CMS
 * @description TODO
 * @create 2023/3/10 9:15
 **/
@Configuration
public class MybatisPlusConfig {
//    @Bean
//    public MybatisPlusInterceptor mybatisPlusInterceptor(){
//        //1 创建MybatisPlusInterceptor拦截器对象
//        MybatisPlusInterceptor mpInterceptor = new MybatisPlusInterceptor();
//        //2 添加分页拦截器
//        mpInterceptor.addInnerInterceptor(new PaginationInnerInterceptor());
//        return mpInterceptor;
//    }

    /**
     *  mybatis-plus分页插件
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }

}
