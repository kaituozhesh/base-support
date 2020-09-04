package com.ktz.sh.base.config;

import com.ktz.sh.base.interceptor.AuthInterceptor;
import com.ktz.sh.base.properties.AuthProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @ClassName InterceptorConfig
 * @Description
 * @Author 开拓者-骚豪
 * @Date 2020/9/3 13:10
 * @Version V1.0.0
 **/
@Configuration
@EnableWebMvc
public class InterceptorConfig implements WebMvcConfigurer {

    @Autowired
    private AuthProperties authProperties;

    @Bean
    public AuthProperties authProperties() {
        return new AuthProperties();
    }

    @Bean
    public AuthInterceptor authInterceptor() {
        return new AuthInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor())
                .addPathPatterns(authProperties.getInclude())
                .excludePathPatterns(authProperties.getExclude());
    }
}
