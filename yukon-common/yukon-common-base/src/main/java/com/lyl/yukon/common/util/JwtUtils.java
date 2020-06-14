package com.lyl.yukon.common.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.lyl.yukon.common.constant.TokenConstant;
import com.lyl.yukon.common.entity.TokenInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Map;

/**
 * <p>jwt工具类</p>
 *
 * @author liaoyl
 * @version 1.0 2019/4/18 18:15
 **/
public class JwtUtils {

    private static Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    /**
     * 失效重新登录时间为7天
     */
    private static final long EXPIRE_TIME = 7 * 24 * 60 * 60 * 1000;
    /**
     * 2小时刷新一次
     */
    private static final long REFRESH_TIME = 2 * 60 * 60 * 1000;
    /**
     * 秘钥
     */
    private static final String SECRET = "BR9JbRVx";

    /**
     * 生成token
     *
     * @param userId       用户id
     * @param terminalType 终端类型
     * @param ip           ip地址
     * @return 加密的token
     */
    public static String sign(String userId, String terminalType, String ip) {
        long timestamp = System.currentTimeMillis();
        return JWT.create()
                .withClaim(TokenConstant.USER_ID, userId)
                .withClaim(TokenConstant.TERMINAL_TYPE, terminalType)
                .withClaim(TokenConstant.CREATE_DATE, timestamp)
                .withClaim(TokenConstant.IP, ip)
                .withExpiresAt(new Date(timestamp + EXPIRE_TIME))
                .sign(Algorithm.HMAC256(userId + SECRET));
    }

    /**
     * 校验token
     *
     * @return 是否正确
     */
    public static boolean verify(String token) {
        DecodedJWT jwt = JWT.decode(token);
        // 获取所有声明
        Map<String, Claim> claims = jwt.getClaims();
        String userId = claims.get(TokenConstant.USER_ID).asString();
        String terminalType = claims.get(TokenConstant.TERMINAL_TYPE).asString();
        long createDate = claims.get(TokenConstant.CREATE_DATE).asLong();
        String ip = claims.get(TokenConstant.IP).asString();
        try {
            // 根据密码生成JWT效验器
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(userId + SECRET))
                    .withClaim(TokenConstant.USER_ID, userId)
                    .withClaim(TokenConstant.TERMINAL_TYPE, terminalType)
                    .withClaim(TokenConstant.CREATE_DATE, createDate)
                    .withClaim(TokenConstant.IP, ip)
                    .build();
            // 效验TOKEN，认证失败时会抛异常
            verifier.verify(token);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return false;
        }
        return true;
    }

    /**
     * 获得token中包含的userId
     */
    public static String getUserId(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim(TokenConstant.USER_ID).asString();
        } catch (JWTDecodeException e) {
            logger.error(e.getMessage(), e);
            return "";
        }
    }

    /**
     * 获得token中的完整信息
     */
    public static TokenInfo getTokenInfo(String token) {
        TokenInfo tokenInfo = new TokenInfo();
        DecodedJWT jwt = JWT.decode(token);
        tokenInfo.setUserId(jwt.getClaim(TokenConstant.USER_ID).asString());
        tokenInfo.setCreateDate(jwt.getClaim(TokenConstant.CREATE_DATE).asDate());
        tokenInfo.setIp(jwt.getClaim(TokenConstant.IP).asString());
        tokenInfo.setTerminalType(jwt.getClaim(TokenConstant.TERMINAL_TYPE).asString());
        return tokenInfo;
    }

    /**
     * 是否过期，抛出异常说明token错误，返回false表示和过期无关
     */
    public static boolean isExpire(Date createDate) {
        try {
            return (createDate.getTime() + EXPIRE_TIME) < System.currentTimeMillis();
        } catch (JWTDecodeException e) {
            logger.error(e.getMessage(), e);
            return true;
        }
    }

    /**
     * 是否需要刷新
     */
    public static boolean needRefresh(Date createDate) {
        try {
            return (createDate.getTime() + REFRESH_TIME) < System.currentTimeMillis();
        } catch (JWTDecodeException e) {
            logger.error(e.getMessage(), e);
            return true;
        }
    }

    /**
     * 过期刷新token
     *
     * @param token 原token
     */
    public static String refresh(String token) {
        long timestamp = System.currentTimeMillis();
        // 从token中获取信息重新生成token
        DecodedJWT jwt = JWT.decode(token);
        Map<String, Claim> claims = jwt.getClaims();
        String userId = claims.get(TokenConstant.USER_ID).asString();
        String terminalType = claims.get(TokenConstant.TERMINAL_TYPE).asString();
        String ip = claims.get(TokenConstant.IP).asString();
        // 生成token
        return JWT.create()
                .withClaim(TokenConstant.USER_ID, userId)
                .withClaim(TokenConstant.TERMINAL_TYPE, terminalType)
                .withClaim(TokenConstant.CREATE_DATE, timestamp)
                .withClaim(TokenConstant.IP, ip)
                .withExpiresAt(new Date(timestamp + EXPIRE_TIME))
                .sign(Algorithm.HMAC256(userId + SECRET));
    }

    public static void main(String[] args) {
        String token = JWT.create()
                .withClaim(TokenConstant.USER_ID, "1")
                .withClaim(TokenConstant.TERMINAL_TYPE, "web")
                .withClaim(TokenConstant.CREATE_DATE, 1555470508000L)
                .withClaim(TokenConstant.IP, "127.0.0.1")
                .withExpiresAt(new Date(1555470508000L + EXPIRE_TIME))
                .sign(Algorithm.HMAC256("1" + SECRET));
    }


}
