package com.lyl.yukon.common.util;

import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * <p>调用用户中心安全相关</p>
 *
 * @author liaoyl
 * @version 1.0 2018/10/24 11:22
 **/
public class SecurityUtils {

    private static Logger logger = LoggerFactory.getLogger(SecurityUtils.class);


    private static final String KEY_ALGORITHM = "AES";
    private static final String CIPHER_ALGORITHM = "AES";
    /**
     * AES密钥
     */
    private static final String AES_CODE = "ccw.dev.123QWE!@#";


    public static String getAesCode() {
        // echostr 取4位及以下随机字符
        String echostr = RandomStringUtils.randomAlphanumeric(4);
        // nonce 取4位及以下随机数字
        Random rd = new Random();
        // 为变量赋随机值1000-9999
        String nonce = String.valueOf(rd.nextInt(9999 - 1000 + 1) + 1000);
        // timestamp为系统时间具体格式例：2016-09-29 12:12:12
        // 初始数据初始数据经AES加密后get形式添加 获取后至服务器请求 时间正负不超过5分钟 即30000S
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String timestamp = sdf.format(new Date());

        // 验证签名证书 signature 验证时间戳 timestamp 当前随机数 nonce 当前随机字符串 echostr 组合形式为：
        //（echostr 初始字段MD5加密） +（timestamp初始字段MD5加密）+（ MD5加密nonce）
        // 注：此处AppUtil为MD5方法 为初始MD5加密方法 仅为封装调用类
        // 此处组合均为 单独加密后拼接
        String signature = MD5(echostr) + MD5(timestamp)
                + MD5(nonce);
        return "signature=" + signature + "&echostr="
                + addSecureToStr(echostr, AES_CODE) + "&timestamp="
                + addSecureToStr(timestamp, AES_CODE) + "&nonce="
                + nonce;
    }

    /**
     * 加密
     */
    public static String MD5(String sourceStr) {
        String result = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(sourceStr.getBytes());
            byte b[] = md.digest();
            int i;
            StringBuffer buf = new StringBuffer();
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0) {
                    i += 256;
                }
                if (i < 16) {
                    buf.append("0");
                }
                buf.append(Integer.toHexString(i));
            }
            result = buf.toString();
        } catch (NoSuchAlgorithmException e) {
            logger.error(e.getMessage(), e);
        }
        return result;
    }

    private static String addSecureToStr(String str, String keyValue) {
        byte[] data = null;
        try {
            data = encrypt(str.getBytes(StandardCharsets.UTF_8), keyValue);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return bytesToHexString(data);
    }

    private static byte[] encrypt(byte[] data, String keyValue) throws Exception {
        byte[] key = MD5(keyValue).getBytes();
        Key secretKey = new SecretKeySpec(key, KEY_ALGORITHM);
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        return cipher.doFinal(data);
    }

    private static String bytesToHexString(byte[] src){
        StringBuilder stringBuilder = new StringBuilder();
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }


}
