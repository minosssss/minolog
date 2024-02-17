package com.minolog.api.config;

import com.minolog.api.config.data.UserSession;
import com.minolog.api.exception.Unauthorized;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class AuthResolver implements HandlerMethodArgumentResolver {
    /**
     * 호출되는 Controller의 파라미터 값을 검사하는 콜백 함수
     *
     * @param parameter 클라이언트로 부터 받은 파라미터
     * @return 적용 여부
     */
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(UserSession.class);
    }

    /**
     * supportsParameter 콜백 함수에서 true를 반환했을 경우
     * 호출되는 콜백 함수
     */
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String accessToken = webRequest.getHeader("Authorization");

        if (accessToken == null || accessToken.isEmpty()) {
            throw new Unauthorized();
        }

        // 사용자 확인
        return new UserSession(1L);
    }
}
