package kr.co.aeye.apiserver.jwt.tokens;

import java.security.Key;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import kr.co.aeye.apiserver.api.user.entity.User;
import lombok.extern.slf4j.Slf4j;
import java.util.Base64;
import java.util.Date;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;


@Slf4j
@Component
public class TokenProvider implements InitializingBean {
    private static final String AUTHORITIES_KEY = "Auth";
    private static final String BEARER_TYPE = "Bearer";
    private final String secret;
    private final long ACCESS_TOKEN_EXPIRE_TIME;
    public final long REFRESH_TOKEN_EXPIRE_TIME;
    private Key key;

    public TokenProvider(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.access-token-expire-time}") long access_token_expire_time,
            @Value("${jwt.refresh-token-expire-time}") long refresh_token_expire_time
            ){
        this.secret = secret;
        this.ACCESS_TOKEN_EXPIRE_TIME = access_token_expire_time;
        this.REFRESH_TOKEN_EXPIRE_TIME = refresh_token_expire_time;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }


    public TokenDto generateTokenDto(User user){
        Claims claims = Jwts.claims().setSubject(user.getId().toString());

        long now = (new Date()).getTime();

        Date accessTokenExpiresIn = new Date(now + ACCESS_TOKEN_EXPIRE_TIME);
        String accessToken = Jwts.builder()
                .setClaims(claims)
                .setExpiration(accessTokenExpiresIn)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        // Refresh Token 생성
        Date refreshTokenExpiresIn = new Date(now + REFRESH_TOKEN_EXPIRE_TIME);
        String refreshToken = Jwts.builder()
                .setClaims(claims)
                .setExpiration(refreshTokenExpiresIn)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return TokenDto.builder()
                .grantType(BEARER_TYPE)
                .accessToken(accessToken)
                .accessTokenExpiresIn(accessTokenExpiresIn.getTime())
                .refreshToken(refreshToken)
                .build();
    }

    // 토큰에서 회원 정보 추출
    public String getUserId(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateToken(String token){
        try{
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        }catch(io.jsonwebtoken.security.SecurityException | MalformedJwtException e){
            log.info("잘못된 JWT 서명입니다.");
        }catch(ExpiredJwtException e){
            log.info("만료된 JWT 토큰입니다.");
        }catch(UnsupportedJwtException e){
            log.info("지원하지 않는 JWT 토큰입니다.");
        }catch(IllegalArgumentException e){
            log.info("JWT 토큰이 잘못되었습니다.");
        }
        return false;
    }
}
