package com.ithe.tushu_guanli_xitong.filter;

import com.alibaba.fastjson.JSON;

import com.ithe.tushu_guanli_xitong.common.BaseContext;
import com.ithe.tushu_guanli_xitong.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/*
检测用户是否已经完成登录
* */
@WebFilter(filterName = "loginCheckFilter", urlPatterns = "/*")
@Slf4j
public class LoginCheckFilter implements Filter {
    //路径匹配器，支持通配符
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;


//        1、获取本次请求的URI
        String requestURI = request.getRequestURI();
        log.info("拦截到请求：{}", requestURI);
//         定义不需要请求路径
        String[] urls = new String[]{
                "/user/login",
                "/user/logout",
                "/user/register",
                "/librarystatic/**",
//                "/**"

        };
//        2、判断本次请求是否需要处理
        boolean check = check(urls, requestURI);
//        3、如果不需要处理，则直接放行
        if (check) {
            log.info("本次请求{}不需要处理", requestURI);
            filterChain.doFilter(request, response);
            return;
        }
//        4、判断登录状态，如果已登录，则直接放行
        if (request.getSession().getAttribute("yonghu") != null) {
            log.info("用户已登录，用户ID为：{}", request.getSession().getAttribute("yonghu"));
            System.out.println("用户已登录，用户ID为：{}"+request.getSession().getAttribute("yonghu"));
            long empid = (Long) request.getSession().getAttribute("yonghu");
            BaseContext.setCurrentId(empid);
            filterChain.doFilter(request, response);
            return;
        }
//        5、如果未登录则返回未登录结果，通过输出流方式向客户端页面响应数据
        log.info("用户未登录");
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        return;
    }

    /*
    路径匹配，检查此次请求是否放行
    * */
    public boolean check(String[] urls, String requestURI) {
        for (String url : urls) {
            if (PATH_MATCHER.match(url, requestURI)) {
                System.out.println(url);
                return true;
            }
        }
        return false;
    }
}
