package com.example.lly.module.security;

import com.example.lly.entity.rbac.User;
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

    //角色的Key值
    private static final String ROLE_CLAIMS = "role";

    //过期时间, 设定为30分钟
    private static final long EXPIRATION = 1800L;

    //如果选择 "记住账号", 过期时间则延长为一个星期
    private static final long EXPIRATION_REMEMBER = 1800L * 2 * 24 * 7;

    //头部
    public static final String TOKEN_HEADER = "Authorization";

    //前缀
    public static final String TOKEN_PREFIX = "Bearer ";

    /**
     * 生成Token令牌
     * @param userDetails 用户信息
     * @param isRememberMe  是否记住账号
     * @return  Token令牌
     */
    public static String createToken(UserDetails userDetails, Boolean isRememberMe) {
        long expirationTime = isRememberMe ? EXPIRATION_REMEMBER : EXPIRATION;
        String username = userDetails.getUsername();
        Collection<? extends GrantedAuthority> roles = userDetails.getAuthorities();
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", username);
        claims.put("createTime", new Date());
        claims.put("isRememberMe", isRememberMe);
        claims.put(ROLE_CLAIMS, roles);
        return createToken(claims, isRememberMe);
    }

    public static String createToken(Map<String, Object> claims, Boolean isRememberMe) {
        long expirationTime = isRememberMe ? EXPIRATION_REMEMBER : EXPIRATION;
        Date expiration = new Date(System.currentTimeMillis() + expirationTime * 1000L);
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
            claims.put("createTime", new Date());
            refreshToken = createToken(claims, (boolean) claims.get("isRememberMe"));
        } catch(Exception e) {
            return null;
        }
        return refreshToken;
    }

    //验证账号一致和是否过期
    public static Boolean validateToken(String token, UserDetails userDetails) {
        User user = (User) userDetails;
        String username = getUsernameFromToken(token);
        return (username.equals(user.getUsername()) && !isExpiration(token));
    }

    public static String getUsernameFromToken(String token) {
        return getTokenBody(token).getSubject();
    }

    public static String getUserRoleFromToken(String token) {
        return (String) getTokenBody(token).get(ROLE_CLAIMS);
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
            claims = Jwts.parser().setSigningKey(SECRET).parseClaimsJwt(token).getBody();
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

}
