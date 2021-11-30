package com.mes.mesBackend.config;

import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtTokenProvider {

    @Value("{jwt.token.secret-key}")
    private String secretKey;

//    @Value("{jwt.token.header}")
//    private String header;

//    private static final Long ACCESS_TOKEN_EXPIRE_TIME = 20 * 1000L;                // 20초
//    private static final Long REFRESH_TOKEN_EXPIRE_TIME = 2 * 60 * 1000L;      // 4분
    private static final String ROLES = "roles";

        private static final Long ACCESS_TOKEN_EXPIRE_TIME = 2 * 60 * 1000L;                // 2분
    private static final Long REFRESH_TOKEN_EXPIRE_TIME = 3 * 60 * 1000L;      // 4분
//    private static final Long REFRESH_TOKEN_EXPIRE_TIME = 7 * 24 * 60 * 60 * 1000L;      // 7일
//    private static final String AUTHORITIES_KEY = "auth";


    private final UserDetailsService userDetailsService;

    // 객체 초기화, secretKey를 Base64로 인코딩한다.
    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    // AccessToken 생성
    public String createAccessToken(Authentication authentication) {
        Long now = new Date().getTime();

        JwtBuilder builder = Jwts.builder();
        // Header
        builder.setHeaderParam("typ", "JWT");       // header "typ": JWT
        builder.signWith(SignatureAlgorithm.HS256, secretKey);  // header "alg": HS256

        // payLoad
        builder.claim(ROLES, authentication.getAuthorities().toString().split(","));      // "roles": "ROLE_USER"
        builder.setSubject(authentication.getName());       // "sub": userCode
        builder.setExpiration(new Date(now + ACCESS_TOKEN_EXPIRE_TIME));    // "exp": 1516239022
        builder.setIssuedAt(new Date());            // "iss": 15162120  토큰 발행시간 정보

        String accessToken = builder.compact();
        return accessToken;
    }

    // RefreshToke 생성
    public String createRefreshToken() {
        Long now = new Date().getTime();
        JwtBuilder builder = Jwts.builder();

        // Header
        builder.setExpiration(new Date((now + REFRESH_TOKEN_EXPIRE_TIME)));
        builder.signWith(SignatureAlgorithm.HS256, secretKey);
        return builder.compact();
    }


    // 토큰 복호화
    // JWT 토큰에서 인증 정보 조회
    public Authentication getAuthenticationFromAccessToken(String accessToken) {
        // 토큰 복호화
        Claims claims = parseClaims(accessToken);

        // claims 에서 권한 정보 가져오기
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(ROLES).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        // UserDetails 객체를 만들어서 Authentication 리턴
        UserDetails principal = new User(claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    // token 의 유효성 + 만료일자 확인
    public Boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            System.out.println(token);
            return true;
        } catch (MalformedJwtException e) {
            log.info("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰이 잘못되었습니다.");
        }
        return false;
    }


//    public TokenDto createToken(UserLogin userLogin) {
//
//
//        // accessToken 생성
//        String accessToken = createJws(ACCESS_TOKEN_EXPIRE_TIME, userLogin);
//        // refreshToken 생성
//        String refreshToken = createJws(REFRESH_TOKEN_EXPIRE_TIME, userLogin);
//
//        TokenDto tokenDto = new TokenDto();
//        tokenDto.setAccessToken(accessToken);
//        tokenDto.setRefreshToken(refreshToken);
//
//        return tokenDto;
//    }
//    public TokenDto createTokenDto(String userCode, List<String> roles) {
//        // 권한들 가져오기
////        String authorities = authentication.getAuthorities().stream()
////                .map(GrantedAuthority::getAuthority)
////                .collect(Collectors.joining(","));
//        Claims claims = Jwts.claims().setSubject(userCode);     // JWT payloac에 저장되는 정보단위
//        claims.put("role", roles);
//
//        Long now = (new Date()).getTime();
//
//        // accessToken 생성
//        Date accessTokenExpiresIn = new Date(now + ACCESS_TOKEN_EXPIRE_TIME);
//        String accessToken = Jwts.builder()
//                .setSubject(userCode)
//                .setClaims(claims)
//                .setExpiration(accessTokenExpiresIn)
//                .signWith(SignatureAlgorithm.HS256, secretKey)
//                .compact();
//
//        // refreshToken 생성
//        String refreshToken = Jwts.builder()
//                .setExpiration(new Date(now + REFRESH_TOKEN_EXPIRE_TIME))
//                .signWith(SignatureAlgorithm.HS256, secretKey)
//                .compact();
//
//        return TokenDto.builder()
//                .grantType(BEARER_TYPE)
//                .accessToken(accessToken)
//                .accessTokenExpiresIn(accessTokenExpiresIn.getTime())
//                .refreshToken(refreshToken)
//                .build();
//    }

//    private String createJws(Authentication authentication) {
//        JwtBuilder builder = Jwts.builder();
//
//        // Header
//        /*
//        * {
//        *   "typ": JWT,
//        *   "alg": HS256"
//        * }
//        * */
//
//        Long now = new Date().getTime();
//        // Header
//        builder.setHeaderParam("typ", "JWT");
//        builder.signWith(HS256, secretKey);
//        builder.claim("roles", authentication.getAuthorities().)
//
//        // PayLoad
//        builder.setSubject(userLogin.getUserCode());
//        builder.setExpiration(new Date(now + expMin));
//        builder.setIssuedAt(new Date());
//
//        // Singature
//
//        String jws = builder.compact();
//        return jws;
//    }



//    public Authentication getAuthentication(String accessToken) throws IOException {
//        // 토큰 복호화
//        Claims claims = parseClaims(accessToken);
//
//        if (claims.get(AUTHORITIES_KEY) == null) {
//            throw new IOException("권한정보가 없는 토큰입니다.");
//        }
//
//        // 클레임에서 권한 정보 가져오기
//        Collection<? extends GrantedAuthority> authorities =
//                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
//                        .map(SimpleGrantedAuthority::new)
//                        .collect(Collectors.toList());
//
//        // UserDetails 객체를 만들어서 Authentication 리턴
//        UserDetails principal = new User(claims.getSubject(), "", authorities);
////
//        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
//    }



//    private Claims parseClaims(String accessToken) {
//        try {
//            return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(accessToken).getBody();
//        } catch (ExpiredJwtException e) {
//            return e.getClaims();
//        }
//    }

    // JWT 토큰 생성
//    public String createToken(String userCode, List<String> roles) {
//        Claims claims = Jwts.claims().setSubject(userCode);// JWT payload 에 저장되는 정보단위
//        claims.put("role", roles);       // 정보는 key-value 쌍으로 저장된다.
//        Date now = new Date();
//        return Jwts.builder()
//                .setClaims(claims)      // 정보저장
//                .setIssuedAt(now)       // 토큰 발행시간 정보
//                .setExpiration(new Date(now.getTime() + ACCESS_TOKEN_EXPIRE_TIME))    // set Expire time
//                .signWith(SignatureAlgorithm.HS256, secretKey)  // 사용할 알고리즘과 signature에 들어갈 secret값
//                .compact();
//    }

//    // JWT 토큰에서 인증 정보 조회
//    public Authentication getAuthentication(String token) {
//        UserDetails userDetails = userDetailsService.loadUserByUsername(getUserCode(token));
//        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
//    }
//
//    // 토큰에서 회원 정보 추출
//    public String getUserCode(String token) {
//        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
//    }
//
//    // Request 의 Header 에서 token 값을 가져옴. "Authorization" : "TOKEN"
//    public String resolveToken(HttpServletRequest request) {
//        return request.getHeader("Authorization");
//    }
//
//    // token 의 유효성 + 만료일자 확인
//    public boolean validateToken(String jwtToken) {
//        try {
//            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken);
//            return !claims.getBody().getExpiration().before(new Date());
//        } catch (Exception e) {
//            return false;
//        }
//    }

}
