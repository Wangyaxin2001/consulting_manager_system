package com.yaxin.cms.web.interceptor;

import com.yaxin.cms.exception.ServiceException;
import com.yaxin.cms.util.JwtUtil;
import com.yaxin.cms.util.ResultCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class JwtInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object object) {
    	// 如果不是映射到方法直接通过
    	if(!(object instanceof HandlerMethod)){
    		return true;
    	}

        // 从 HTTP请求头中取出 token
        String token = httpServletRequest.getHeader("Authorization");
        if (token == null) {
            throw new RuntimeException("无token，请重新登录");
        }

        // 验证 token
        try {
            //解析JWT
            Claims claims = JwtUtil.parseJWT(token);
            String id = String.valueOf(claims.get("userId"));
            //专门为 /auth/user/info 提供服务
            httpServletRequest.setAttribute("userId",id);
        }catch (ExpiredJwtException e){
            //登录到期
            throw new RuntimeException("登录到期");
        }catch (MalformedJwtException e){
            //令牌失效
            throw new RuntimeException("令牌失效");
        }catch (Exception e){
            log.error(e.getMessage());
            //服务器内部错误
            throw new ServiceException(ResultCode.SYSTEM_INNER_ERROR);
        }


        //下面为测试输出代码，实际开发中可以删除
        // 验证通过后，取出 JWT中存放的数据
        // 获取 token 中的 userId
        /*String userId = JwtUtil.getUserId(token);
        System.out.println("id: " + userId);
        
        // 获取并输出 token 中的 其他数据
        Map<String, Object> info = JwtUtil.getInfo(token);
        info.forEach((k,v)->System.out.println(k + ": " + v));*/

        return true;
    }
}