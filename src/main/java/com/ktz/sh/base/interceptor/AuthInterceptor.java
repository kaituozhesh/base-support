package com.ktz.sh.base.interceptor;

import cn.hutool.crypto.SecureUtil;
import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.util.*;


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
        log.info("Request BODY : " + body);
        try {
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> reqMap = mapper.readValue(body, new TypeReference<Map<String, Object>>() {
            });
            Map<String, String[]> parameterMap = request.getParameterMap();
            parameterMap.forEach((k, v) -> reqMap.put(k, v[0]));
            return !request.getHeader(RequestHeaderEnum.SIGN.getFieldName()).equals(sign(reqMap, authProperties.getSalt(), request.getHeader(RequestHeaderEnum.TIMESTAMP.getFieldName())));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
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
        keyList.forEach(key -> builder.append(key).append(JSONUtils.toJsonString62(map.get(key))));
        builder.append(RequestHeaderEnum.TIMESTAMP.getFieldName()).append(timestamp);
        String sign = SecureUtil.md5(SecureUtil.md5(builder.toString()) + salt);
        log.info("SIGN : " + sign);
        return sign;
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

        String str = "{\"id\":100174,\"departmentId\":null,\"username\":\"snjd\",\"uuid\":null,\"password\":\"e461c7f45cd1e0db023956829cc676714ecbbbca9ad25cf43a25fb52b8373f58\",\"name\":\"首南街道\",\"idCardNo\":null,\"gender\":null,\"birthday\":null,\"mobile\":\"111111\",\"phoneNo\":null,\"headImg\":null,\"nation\":null,\"country\":null,\"province\":\"330000\",\"city\":\"330200\",\"area\":\"330212\",\"street\":\"330212006\",\"address\":null,\"modifyTime\":1598439872000,\"createTime\":1575002807000,\"deleted\":\"0\",\"mobileCornet\":null,\"email\":null,\"level\":null,\"createId\":21,\"modeifyId\":18,\"type\":\"普通用户\",\"registrationid\":null,\"permissionArea\":null,\"token\":null,\"department\":null,\"roles\":[{\"id\":111}],\"menus\":[{\"id\":2,\"parentId\":1,\"name\":\"处置概况\",\"url\":\"situationDisposal\",\"remark\":null,\"moduleId\":\"\",\"level\":\"2\",\"icon\":null,\"sort\":null,\"isConfig\":0,\"deleted\":null,\"menus\":null},{\"id\":10,\"parentId\":9,\"name\":\"风险监测\",\"url\":\"riskMonitor\",\"remark\":null,\"moduleId\":\"\",\"level\":\"2\",\"icon\":null,\"sort\":null,\"isConfig\":0,\"deleted\":null,\"menus\":null},{\"id\":13,\"parentId\":12,\"name\":\"类金融机构\",\"url\":\"financialInstitutions\",\"remark\":null,\"moduleId\":\"\",\"level\":\"3\",\"icon\":null,\"sort\":null,\"isConfig\":0,\"deleted\":null,\"menus\":null},{\"id\":16,\"parentId\":15,\"name\":\"关联关系\",\"url\":\"incidenceRelation\",\"remark\":null,\"moduleId\":\"\",\"level\":\"3\",\"icon\":null,\"sort\":null,\"isConfig\":0,\"deleted\":null,\"menus\":null},{\"id\":28,\"parentId\":27,\"name\":\"事件上报\",\"url\":\"eventsReported\",\"remark\":null,\"moduleId\":\"\",\"level\":\"3\",\"icon\":null,\"sort\":null,\"isConfig\":0,\"deleted\":null,\"menus\":null},{\"id\":32,\"parentId\":31,\"name\":\"机构搜索\",\"url\":\"institutionSearch\",\"remark\":null,\"moduleId\":\"\",\"level\":\"3\",\"icon\":null,\"sort\":null,\"isConfig\":0,\"deleted\":null,\"menus\":null},{\"id\":36,\"parentId\":35,\"name\":\"感知概况\",\"url\":\"discovery\",\"remark\":null,\"moduleId\":\"\",\"level\":\"2\",\"icon\":null,\"sort\":null,\"isConfig\":0,\"deleted\":null,\"menus\":null},{\"id\":44,\"parentId\":43,\"name\":\"预警概况\",\"url\":\"situationWarning\",\"remark\":null,\"moduleId\":\"\",\"level\":\"2\",\"icon\":null,\"sort\":null,\"isConfig\":0,\"deleted\":null,\"menus\":null},{\"id\":48,\"parentId\":47,\"name\":\"数据概况\",\"url\":\"dataOverview\",\"remark\":null,\"moduleId\":\"\",\"level\":\"2\",\"icon\":null,\"sort\":null,\"isConfig\":0,\"deleted\":null,\"menus\":null},{\"id\":54,\"parentId\":53,\"name\":\"个人中心\",\"url\":\"selfCenter\",\"remark\":null,\"moduleId\":\"\",\"level\":\"2\",\"icon\":null,\"sort\":null,\"isConfig\":0,\"deleted\":null,\"menus\":null},{\"id\":60,\"parentId\":59,\"name\":\"机构管理\",\"url\":\"institutionManage\",\"remark\":null,\"moduleId\":\"\",\"level\":\"3\",\"icon\":null,\"sort\":null,\"isConfig\":0,\"deleted\":null,\"menus\":null},{\"id\":70,\"parentId\":69,\"name\":\"合同存证\",\"url\":\"contractDeposit\",\"remark\":null,\"moduleId\":\"\",\"level\":\"2\",\"icon\":null,\"sort\":null,\"isConfig\":0,\"deleted\":null,\"menus\":null},{\"id\":76,\"parentId\":75,\"name\":\"考核总览\",\"url\":\"examinationOverview\",\"remark\":null,\"moduleId\":\"\",\"level\":\"2\",\"icon\":null,\"sort\":null,\"isConfig\":0,\"deleted\":null,\"menus\":null},{\"id\":84,\"parentId\":59,\"name\":\"排查日志\",\"url\":\"institutionExamine\",\"remark\":null,\"moduleId\":\"\",\"level\":\"3\",\"icon\":null,\"sort\":null,\"isConfig\":1,\"deleted\":null,\"menus\":null},{\"id\":59,\"parentId\":53,\"name\":\"后台管理\",\"url\":\"backstageManage\",\"remark\":null,\"moduleId\":\"\",\"level\":\"2\",\"icon\":null,\"sort\":null,\"isConfig\":1,\"deleted\":null,\"menus\":null},{\"id\":88,\"parentId\":59,\"name\":\"三到三核\",\"url\":\"threeNuclear\",\"remark\":null,\"moduleId\":\"\",\"level\":\"3\",\"icon\":null,\"sort\":null,\"isConfig\":1,\"deleted\":null,\"menus\":null}],\"resources\":[{\"id\":2,\"name\":\"app操作权限\",\"code\":null,\"url\":\"appOperation\",\"menuId\":54,\"comment\":\"个人中心-app\",\"sort\":null,\"createTime\":1562297338000}],\"loginTime\":12,\"isFirstLogin\":null,\"provinceName\":null,\"cityName\":null,\"areaName\":null,\"streetName\":null,\"departmentName\":null,\"groundUserId\":null,\"tenantUserId\":null,\"financeDeptDto\":null,\"deptIdNull\":false,\"areaUser\":false}";
        Map<String, Object> reqMap = new HashMap<>();
//        reqMap= JSONObject.parseObject(str, new TypeReference<Map<String, Object>>() {}.getType());
        ObjectMapper mapper = new ObjectMapper();
        try {
            reqMap = mapper.readValue(str, new TypeReference<Map<String, Object>>() {
            });
            System.out.println();
        } catch (IOException e) {
            e.printStackTrace();
        }
        reqMap.put("username", "nbsjrb");
        reqMap.put("password", "bd3f2934465386ad39a83f768fec9c288bd32d1d00d7c9131b6ab7f373f94b2a");
        System.out.println(sign(reqMap, "finance", "1599201695798"));

    }
}
