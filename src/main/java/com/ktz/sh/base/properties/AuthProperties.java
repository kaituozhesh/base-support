package com.ktz.sh.base.properties;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * @ClassName AuthInterceptor
 * @Description 请求认证参数配置
 * @Author 开拓者-骚豪
 * @Date 2020/9/3 12:08
 * @Version V1.0.0
 **/
@Slf4j
@Data
@Component
@AutoConfigureOrder(Integer.MIN_VALUE)
@ConfigurationProperties(prefix = "spring.security.auth")
public class AuthProperties {

    private String salt;

    private List<String> include = Collections.singletonList("/**");

    private List<String> exclude;

    /**
     * 请求有效时间  单位毫秒
     */
    private long validTime = 60_000_000;

}
