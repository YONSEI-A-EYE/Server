package kr.co.aeye.apiserver.oauth.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.co.aeye.apiserver.jwt.tokens.TokenDto;
import kr.co.aeye.apiserver.jwt.tokens.TokenProvider;
import kr.co.aeye.apiserver.jwt.utils.CookieUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
//import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames.REFRESH_TOKEN;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthSuccessHandler implements AuthenticationSuccessHandler {

    /*
    login success
    1. create refresh token and access token
    2. set refresh token in set-cookie
    3. return response with access token(response body with Bearer) and refresh token(set-cookie)

    log out
    1. refresh token -> black list
    2. clear set-cookie

    api - refresh token validation check
    - refresh black list?
    - access token, refresh token validation check
     */

    private final TokenProvider tokenProvider;
    private final ObjectMapper objectMapper;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        TokenDto token = tokenProvider.generateTokenDto(authentication);
        log.info("token={}", token);

        int cookieMaxAge = (int) tokenProvider.REFRESH_TOKEN_EXPIRE_TIME / 60;

        CookieUtil.deleteCookie(request, response, REFRESH_TOKEN);
        CookieUtil.addCookie(response, REFRESH_TOKEN, token.getRefreshToken(), cookieMaxAge);

        response.addHeader("Authorization", "Bearer " + token.getAccessToken());
        response.setContentType("application/json;charset=UTF-8");

        var writer = response.getWriter();
        writer.println(objectMapper.writeValueAsString(token));
        writer.flush();
    }
}
