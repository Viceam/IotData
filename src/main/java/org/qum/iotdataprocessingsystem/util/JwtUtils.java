package org.qum.iotdataprocessingsystem.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

/**
 * JwtUtils 类提供了生成和解析 JSON Web Tokens (JWT) 的工具方法。
 * 该类使用 HMAC SHA-256 算法进行签名，并包含一个固定的密钥和过期时间。
 *
 * @author viceam
 */
public class JwtUtils {
    /**
     * 密钥用于 JWT 的签名过程，确保令牌的安全性。
     */
    private static final String SECRET_KEY = "ia_@ccaodjADAUD";

    /**
     * 过期时间设置为两天（以毫秒为单位）。
     */
    private static final long expirationTimeInMillis = 2 * 24 * 3600 * 1000;

    /**
     * 根据用户名和角色生成一个新的 JWT 令牌。
     *
     * @param username 用户名，将被存储在 JWT 的主题字段中。
     * @param role     用户的角色信息，作为一个自定义声明存储在 JWT 中。
     * @return 返回生成的 JWT 字符串。
     */
    public static String generateToken(String username, String role) {
        return Jwts.builder()
                .setSubject(username)
                .claim("role", role) // 存储用户角色
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTimeInMillis))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    /**
     * 解析给定的 JWT 令牌并返回其中的有效负载。
     *
     * @param token 要解析的 JWT 字符串。
     * @return 如果令牌有效，则返回 JWT 的 Claims 对象。
     */
    public static Claims parseToken(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }
}





