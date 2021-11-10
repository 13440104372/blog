package com.zbw.blog.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.zbw.blog.pojo.User;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;

/**
 * @author 17587
 */
@Component
@ConfigurationProperties(prefix = "authentication.jwt")
public class JwtProvider {

    private int expireDay;

    private String secret;

    private String header;

    private String author;

    private String redisKeyPrefix;

    public String generateToken(String userName){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH,expireDay);
        return JWT.create()
                // 发布者
                .withIssuer(author)
                // 生成签名的时间
                .withIssuedAt(new Date())
                // 生成签名的有效期,天
                .withExpiresAt(calendar.getTime())
                // 插入数据
                .withClaim(User.TAG,userName)
                .sign(Algorithm.HMAC256(secret));
    }

    public DecodedJWT verifyToken(String token) throws JWTVerificationException {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret))
                    .withIssuer(author)
                    .build();
            return verifier.verify(token);
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public int getExpireDay() {
        return expireDay;
    }

    public void setExpireDay(int expireDay) {
        this.expireDay = expireDay;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getRedisKeyPrefix() {
        return redisKeyPrefix;
    }

    public void setRedisKeyPrefix(String redisKeyPrefix) {
        this.redisKeyPrefix = redisKeyPrefix;
    }
}
