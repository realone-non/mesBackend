package com.mes.mesBackend.config;

import com.mes.mesBackend.interceptor.Interceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {
    private final Interceptor interceptor;
    /*
    * addMapping: 모든 경로에 대해 cors 적용
    * exposedHeaders: 브라우저에 표시할 헤더를 명시, JWT 로그인을 위해서 클라이언트가 헤더에 접근할 수 있게 적용
    * allowOrigins: 서버의 자원에 접근할 수 있는 출처를 명시한다.
    * allowCredentials: 같은 출처라면 기본적으로 쿠키가 request 헤더에 자동으로 설정된다.
    * 하지만 CORS 는 다른 출처간의 통신이기 때문에 자동으로 설정되지 않음. 응답헤더에 true 주면 클라이언트에서 보낸 쿠키를 받을 수 있다.
    * allowedMethods: 요청이 허용되는 메소드 지정
    * */

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods(
                        HttpMethod.OPTIONS.name(),
                        HttpMethod.GET.name(),
                        HttpMethod.HEAD.name(),
                        HttpMethod.POST.name(),
                        HttpMethod.PATCH.name(),
                        HttpMethod.PUT.name(),
                        HttpMethod.DELETE.name()
                );
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(interceptor).addPathPatterns("/**");
    }
}
