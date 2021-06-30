package com.atroot.crowd.util;

import com.atroot.crowd.constant.CrowdConstant;

import javax.servlet.http.HttpServletRequest;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Description:
 *
 * @author atroot@126.com  @ZYD
 * @create 2021.5.25 9:17
 */
public class CrowdUtil {

    /**
     * 判断当前请求的类型
     *
     * @param request 传入一个请求
     * @return true为Ajax false则不是Ajax
     */
    public static boolean judgeRequestType(HttpServletRequest request) {
        String acceptHeader = request.getHeader("Accept");
        String xRequestHeader = request.getHeader("X-Requested-With");

        return (acceptHeader != null && acceptHeader.contains("application/json"))
                ||
                (xRequestHeader != null && xRequestHeader.contains("XMLHttpRequest"));
    }

    /**
     * 加密密码方法
     * @param source 传入一个源字符串密码
     * @return 加密后的16进制的字符串
     */
    public static String md5(String source) {
        // 1、判断来源是否是有效的
        if (source == null || source.length() == 0) {
            throw new RuntimeException(CrowdConstant.MESSAGE_STRING_INVALIDATE);
        }
        // 2、定义加密方式
        String algorithm = "md5";
        try {
            // 3、获取MessageDigest对象实例
            MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
            // 4、将源字符串数据转化为数组
            byte[] sourceBytes = source.getBytes();
            // 5、开始加密
            byte[] output = messageDigest.digest(sourceBytes);
            // 6、创建BigInteger类型对象
            int sigbum = 1;
            BigInteger bigInteger = new BigInteger(sigbum, output);
            // 7、将BigInteger类型的数据转换为字符串类型（按照16进制）
            int radix = 16;
            String encoded = bigInteger.toString(radix);
            return encoded;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
}
