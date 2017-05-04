package com.mengyan.xin.web.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.util.StringUtils;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * 获取客户端Ip、验证码验证类
 */
@Slf4j
public class CaptchaUtil {

    private final static String VERIFY_CODE = "verifyCode";

    public static String obtainClientIP(HttpServletRequest request){
        //获取客户端ip
        String clientIp = request.getHeader("x-forwarded-for");
        if(clientIp == null || clientIp.length() == 0 || "unknown".equalsIgnoreCase(clientIp)) {
            clientIp = request.getHeader("Proxy-Client-IP");
        }
        if(clientIp == null || clientIp.length() == 0 || "unknown".equalsIgnoreCase(clientIp)) {
            clientIp = request.getHeader("WL-Proxy-Client-IP");
        }
        if (clientIp == null || clientIp.length() == 0 || "unknown".equalsIgnoreCase(clientIp)) {
            clientIp = request.getHeader("HTTP_CLIENT_IP");
        }
        if (clientIp == null || clientIp.length() == 0 || "unknown".equalsIgnoreCase(clientIp)) {
            clientIp = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if(clientIp == null || clientIp.length() == 0 || "unknown".equalsIgnoreCase(clientIp)) {
            clientIp = request.getRemoteAddr();
        }
        return clientIp;
    }

    /**
     * 生成验证码
     */
    public static void generate(HttpServletRequest request, HttpServletResponse response) {
        try{
            log.info("获取图片验证码开始时间：{}", WebDateUtil.getToDay(WebDateUtil.DATE_TIME_FORMAT));
            //设置页面不缓存
            response.setHeader("Pragma", "no-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expires", 0);
            String verifyCode = VerifyCodeUtil.generateTextCode(VerifyCodeUtil.TYPE_NUM_LOWER, 4, null);
            //将验证码放到HttpSession里面
            request.getSession().setAttribute(VERIFY_CODE, verifyCode);
            log.info("本次生成的验证码为:"+verifyCode);
            //设置输出的内容的类型为JPEG图像
            response.setContentType("image/jpeg");
            Color c = new Color(0x6FA940);
            BufferedImage bufferedImage = VerifyCodeUtil.generateImageCode(verifyCode,90, 29, 3, true, Color.white, c, null);
            //写给浏览器
            ImageIO.write(bufferedImage, "JPEG", response.getOutputStream());
        }catch(Exception e){
            log.error("获取随机验证码失败！"+e.getMessage());
        }finally {
            log.info("获取图片验证码结束时间：{}", WebDateUtil.getToDay(WebDateUtil.DATE_TIME_FORMAT));
        }
    }

    /**
     * 仅能验证一次，验证后立即删除session
     * @param request 请求
     * @param userInputCaptcha 用户输入的验证码
     * @return 验证通过返回 true, 否则返回 false
     */
    public static boolean validate(HttpServletRequest request, String userInputCaptcha) {
        HttpSession session = request.getSession(false);
        if (null == session) {
            return false;
        }
        // 转成大写重要
        userInputCaptcha = userInputCaptcha.toUpperCase();
        String code = (String) session.getAttribute(VERIFY_CODE);

        boolean result = false ;
        if(StringUtils.hasLength(code)){
            result = userInputCaptcha.equals(code.toUpperCase());
        }

        if (result) {
            session.removeAttribute(VERIFY_CODE);
        }
        return result;
    }
}
