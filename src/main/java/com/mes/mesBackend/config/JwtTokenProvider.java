package com.mes.mesBackend.config;

import com.mes.mesBackend.exception.NotFoundException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Component
public class JwtTokenProvider {

    @Value("{jwt.token.secret-key}")
    private String secretKey;

    @Value("{jwt.token.expiration-time")
    private long tokenValidTime;

    @Value("{jwt.token.header}")
    private String header;

    @Value("{jwt.token.prefix}")
    private String headerPrefix;

    private final UserDetailsService userDetailsService;

    // 객체 초기화, secretKey를 Base64로 인코딩한다.
    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    // JWT 토큰 생성
    public String createToken(String userCode, List<String> roles) {
        Claims claims = Jwts.claims().setSubject(userCode);// JWT payload 에 저장되는 정보단위
        claims.put("role", roles);       // 정보는 key-value 쌍으로 저장된다.
        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims)      // 정보저장
                .setIssuedAt(now)       // 토큰 발행시간 정보
                .setExpiration(new Date(now.getTime() + tokenValidTime))    // set Expire time
                .signWith(SignatureAlgorithm.HS256, secretKey)  // 사용할 알고리즘과 signature에 들어갈 secret값
                .compact();
    }

    // JWT 토큰에서 인증 정보 조회
    public Authentication getAuthentication(String token) throws NotFoundException {
        UserDetails userDetails = userDetailsService.loadUserByUsername(this.getUserCode(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    // 토큰에서 회원 정보 추출
    public String getUserCode(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    // Request 의 Header 에서 token 값을 가져옴. "Authorization" : "TOKEN"
    public String resolveToken(HttpServletRequest request) {
        return request.getHeader(header);
    }

    // token 의 유효성 + 만료일자 확인
    public boolean validateToken(String jwtToken) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

}
