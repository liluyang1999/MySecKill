package com.example.lly.module.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Data
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtTokenUtil implements Serializable {

    //签发者和秘钥
    private static final String ISSUER = "liluyang1999";
    private static final String SECRET = "JwtSecret";    //加密的盐

    private static final String KEY_USERNAME = "username";
    private static final String KEY_CREATETIME = "createTime";

    public static final String errorMsg = "令牌为空、过期或错误, 请重新登录";

    //过期时间, 设定为30分钟
    private static final long EXPIRATION = 1800L;

    //Token的Key值
    public static final String TOKEN_HEADER = "Authorization";
    public static final String BODY_KEY = "authorization";

    //前缀
    public static final String TOKEN_PREFIX = "Bearer ";
    //Key值
    private static final String KEY_ROLES = "Role ";

    /**
     * 生成Token令牌
     *
     * @param userDetails 用户信息
     * @return Token令牌
     */
    public static String createToken(UserDetails userDetails) {
        String username = userDetails.getUsername();
        Collection<? extends GrantedAuthority> roles = userDetails.getAuthorities();
        Map<String, Object> claims = new HashMap<>();
        claims.put(KEY_USERNAME, username);       //我是谁
        claims.put(KEY_CREATETIME, new Date());   //什么时候创立的
        claims.put(KEY_ROLES, roles);              //拥有什么权限
        return createToken(claims);
    }

    public static String createToken(Map<String, Object> claims) {
        Date expiration = new Date(System.currentTimeMillis() + EXPIRATION * 1000L);
        return Jwts.builder().setClaims(claims)
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();
    }


    public static String refreshToken(String token) {
        //对Date进行了更新
        String refreshToken;
        try {
            Claims claims = getClaimsFromToken(token);
            claims.put("createTime", new Date().toString());
            refreshToken = createToken(claims);
        } catch (Exception e) {
            return null;
        }
        return refreshToken;
    }


//    //验证账号一致和是否过期
//    public static Boolean validateToken(String token, UserDetails userDetails) {
//        User user = (User) userDetails;
//        String username = getUsernameFromToken(token);
//        return (username.equals(user.getUsername()) && !isExpiration(token));
//    }

    public static String getUsernameFromToken(String token) {
        String username;
        try {
            Claims claims = getClaimsFromToken(token);
            username = claims.get(KEY_USERNAME, String.class);
        } catch (Exception e) {
            username = null;
        }
        return username;
    }


    public static boolean isExpiration(String token) {
        //和现在时间比较查看是否过期
        return getTokenBody(token).getExpiration().before(new Date());
    }

    /**
     * 令牌中获取数据声明 claims里面包含了权限
     * @param token 令牌
     * @return 数据声明
     */
    private static Claims getClaimsFromToken(String token) {
        Claims claims;
        try {
            claims = getTokenBody(token);
        } catch(Exception e) {
            claims = null;
        }
        return claims;
    }

    private static Claims getTokenBody(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET)
                .parseClaimsJws(token)
                .getBody();
    }


    public static void main(String[] args) {
        String token = "eyJhbGciOiJIUzUxMiJ9.eyJyb2xlIjpbeyJhdXRob3JpdHkiOiJST0xFX2FkbWluOml0ZW0ifV0sImNyZWF0ZVRpbWUiOjE2MTIzNTc5NzEzNDEsInJlbWVtYmVyTWUiOnRydWUsImV4cCI6MTYxMjk2Mjc3MSwidXNlcm5hbWUiOiJ6aGFuZ3NhbiJ9.mp2Eemr4HseqhNSq9DylPWJPlr6ByjaN6uAJ7Y_EVS-uJitN2IJ7bXZMDGWAhESf4dv98NQYNOkJQOquOVZwaw";
        System.out.println(JwtTokenUtil.getUsernameFromToken(token));
        System.out.println(JwtTokenUtil.getTokenBody(token));
        System.out.println(JwtTokenUtil.getTokenBody(token).getExpiration());
    }

    public static Collection<? extends GrantedAuthority> getUserRoleFromToken(String token) {
        Collection<? extends GrantedAuthority> roles;
        try {
            Claims claims = getClaimsFromToken(token);
            roles = (Collection<? extends GrantedAuthority>) claims.get(KEY_ROLES);
        } catch (Exception e) {
            roles = null;
        }
        return roles;
    }

}
