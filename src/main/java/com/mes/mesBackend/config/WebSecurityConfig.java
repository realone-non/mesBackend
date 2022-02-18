package com.mes.mesBackend.config;

import com.mes.mesBackend.auth.JwtAccessDeniedHandler;
import com.mes.mesBackend.auth.JwtAuthenticationEntryPoint;
import com.mes.mesBackend.auth.JwtAuthenticationFilter;
import com.mes.mesBackend.auth.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Collections;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                .csrf().disable()
                // custom exception 추가
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler)
                .and().cors().configurationSource(new CorsConfigurationSource() {
                    @Override
                    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                        CorsConfiguration config = new CorsConfiguration();
                        config.setAllowedOrigins(   // 서버의 자원에 접근할 수 있는 출처를 명시한다. 와일드카드(*) 를 사용하면 아무 출처에서나 서버의 자원에 접근할 수 있다.
                                Arrays.asList(
                                        "http://localhost:3000",
                                        "http://dev-mes-grid.s3-website.ap-northeast-2.amazonaws.com",
                                        "http://prod-mes-grid.s3-website.ap-northeast-2.amazonaws.com",
                                        "http://prod-mes.s3-website.ap-northeast-2.amazonaws.com"
                                )
                        );
                        // 요청이 허용되는 메소드를 지정한다. 와일드카드(*)를 통해 모든 메소더를 허용할 수 있다.
                        config.setAllowedMethods(Collections.singletonList("*"));
                        // 같은 출처라면 기본적으로 쿠키가 request 헤더에 자동으로 설정된다. 하지만 CORS 는 다른 출처간의 통신이기 때문에 자동으로 설정되지 않음. 응답헤더에 true 주면 클라이언트에서 보낸 쿠키를 받을 수 있다.
                        config.setAllowCredentials(true);
                        // 요청이 허용되는 메소드를 지정한다. 와일드카드(*)를 통해 모든 메소더를 허용할 수 있다.
                        config.setAllowedHeaders(Collections.singletonList("*"));
                        // 브라우저에게 표시할 헤더를 명시한다. 브라우저에서 확인할 수 있게 하려면 이렇게 명시 하면 됨.
                        config.setExposedHeaders(Collections.singletonList(AUTHORIZATION));
                        // preflight 요청 결과를 캐시할 수 있는 시간을 의미한다.
                        config.setMaxAge(3600L);
                        return config;
                    }
                }).and()
                // 시큐리티는 세션을 사용하지만 jwt로 인해서 세션을 사용하지 않기 때문에 stateless로 설정
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                // 요청에 대한 사용권한 체크
                .and()
                .authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/swagger-ui/**").permitAll()
                .antMatchers("/api-docs/**").permitAll()
                .antMatchers("/auth/signin").permitAll()
                .antMatchers("/auth/signup").permitAll()
                .antMatchers("/auth/reissue").permitAll()
                .antMatchers("/auth/reset-password").permitAll()
                .antMatchers("/pop/work-processes").permitAll()
                .antMatchers("/label-prints/**").permitAll()
                .antMatchers("/**").authenticated()
//                .anyRequest().permitAll()       // 그 외 나머지 요청은 누구나 접근
                // JwtFilter 를 addFilterBefore 로 등록했던 JwtAuthenticationFilter 클래스를 적용
                .and()
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);
    }
}
