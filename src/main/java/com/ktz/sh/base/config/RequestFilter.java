package com.ktz.sh.base.config;

import com.ktz.sh.base.interceptor.RequestWrapper;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @ClassName RequestFilter
 * @Description
 * @Author 开拓者-骚豪
 * @Date 2020/9/3 13:13
 * @Version V1.0.0
 **/
@Component
@WebFilter(filterName = "requestFilter", urlPatterns = "/**")
public class RequestFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        ServletRequest requestWrapper = new RequestWrapper(request);
        filterChain.doFilter(requestWrapper, response);
    }
}
