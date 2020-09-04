package com.ktz.sh.base.interceptor;

import cn.hutool.crypto.SecureUtil;
import com.alibaba.fastjson.JSON;
import com.ktz.sh.base.enums.RequestHeaderEnum;
import com.ktz.sh.base.properties.AuthProperties;
import com.ktz.sh.base.response.ResponseCode;
import com.ktz.sh.base.response.ResponseData;
import com.ktz.sh.base.util.JSONUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;


/**
 * @ClassName AuthInterceptor
 * @Description 请求认证拦截器 防篡改，防重放
 * @Author 开拓者-骚豪
 * @Date 2020/9/3 11:59
 * @Version V1.0.0
 **/
@Slf4j
@Component
public class AuthInterceptor extends HandlerInterceptorAdapter {

    private static AuthProperties authProperties;

    @Resource
    public void setAuthProperties(AuthProperties authProperties) {
        AuthInterceptor.authProperties = authProperties;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 请求时的时间戳
        String timestamp = request.getHeader(RequestHeaderEnum.TIMESTAMP.getFieldName());

        if (stampVerify(timestamp)) {
            returnFail(ResponseCode.SIGN_EXPIRED, response);
            return false;
        } else if (signVerify(request)) {
            returnFail(ResponseCode.SIGN_ERROR, response);
            return false;
        }

        return false;
    }


    /**
     * 验证是否重放请求
     *
     * @param timestamp
     * @return
     */
    private boolean stampVerify(String timestamp) {
        if (StringUtils.isEmpty(timestamp) || !timestamp.matches("^\\d+$")) {
            return true;
        }
        return timestamp.compareTo(String.valueOf(System.currentTimeMillis() - authProperties.getValidTime())) < 0;
    }

    /**
     * 签名验证
     *
     * @return
     */
    private boolean signVerify(HttpServletRequest request) {

        String sign = request.getHeader(RequestHeaderEnum.SIGN.getFieldName());
        if (StringUtils.isEmpty(sign)) {
            return true;
        }
        String body = new RequestWrapper(request).getBodyString(request);
        Map<String, Object> reqMap = JSONUtils.jsonToMap(body);


        Map<String, String[]> parameterMap = request.getParameterMap();
        parameterMap.forEach((k, v) -> reqMap.put(k, v[0]));

        return !request.getHeader(RequestHeaderEnum.SIGN.getFieldName()).equals(sign(reqMap, authProperties.getSalt(), request.getHeader(RequestHeaderEnum.TIMESTAMP.getFieldName())));
    }

    /**
     * 所有参数排序拼接排序后加上请求时间戳进行第一次MD5加密
     * 参数加密所得结果加上SALT进行第二次MD5加密
     *
     * @param map
     * @param salt
     * @param timestamp
     * @return
     */
    private static String sign(Map<String, Object> map, String salt, String timestamp) {
        if (map == null) {
            return null;
        }
        List<String> keyList = new ArrayList<>(map.keySet());
        Collections.sort(keyList);
        StringBuffer builder = new StringBuffer();
        keyList.forEach(key -> builder.append(key).append(map.get(key)));
        builder.append(RequestHeaderEnum.TIMESTAMP.getFieldName()).append(timestamp);
        return SecureUtil.md5(SecureUtil.md5(builder.toString()) + salt);
    }

    /**
     * 非法请求
     */
    private void returnFail(ResponseCode responseCode, HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        response.getWriter().write(JSON.toJSONString(ResponseData.fail(responseCode, responseCode.getMsg())));
    }

    public static void main(String[] args) {
        System.out.println(System.currentTimeMillis());

        String str = "{\"pageNum\":1,\"pageSize\":10,\"progresses\":[1,2],\"title\":\"\",\"sortField\":\"create_time\",\"sortOrder\":\"desc\",\"userId\":\"18\"}";
        Map<String, Object> reqMap = JSONUtils.jsonToMap(str);
        StringBuilder builder = new StringBuilder();
        reqMap.forEach((k, v) -> builder.append(k).append(v));
        System.out.println(sign(reqMap, "finance", "1599110284549"));
    }
}
