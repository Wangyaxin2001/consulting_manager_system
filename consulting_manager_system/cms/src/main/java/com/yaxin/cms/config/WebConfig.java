package com.yaxin.cms.config;

import com.yaxin.cms.web.interceptor.JwtInterceptor;
import com.yaxin.cms.web.interceptor.VipCheckInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//使用@Configuration注解和代码，替代xml文件进行配置
@Configuration
public class WebConfig implements WebMvcConfigurer {

    // 2.往spring容器中注入vip拦截器对象
    @Autowired
    private VipCheckInterceptor vipCheckInterceptor;

    /**
     * 添加jwt拦截器,并指定拦截路径
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor())
                .addPathPatterns("/auth/**")
                //查询所有一级栏目及其二级栏目的接口不需要被校验(供前台使用)
                .excludePathPatterns("/auth/category/queryAllParent",
                        "/auth/comment/queryByArticleId/{id}");

        //3.注册vip拦截器对象并设置拦截路径
        registry.addInterceptor(vipCheckInterceptor)
                .addPathPatterns("/auth/article/**");
    }

    /**
     * 创建jwt拦截器对象并加入spring容器
     */
    @Bean
    public JwtInterceptor jwtInterceptor() {
        return new JwtInterceptor();
    }

    // 跨域配置: 通过跨域过滤器实现
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        CorsConfiguration config = new CorsConfiguration();

        // 允许跨域的头部信息
        config.addAllowedHeader("*");
        // 允许跨域的方法
        config.addAllowedMethod("*");
        // 可访问的外部域
        config.addAllowedOrigin("*");
        // 需要跨域用户凭证（cookie、HTTP认证及客户端SSL证明等）
        //config.setAllowCredentials(true);
        //config.addAllowedOriginPattern("*");

        // 跨域路径配置
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
