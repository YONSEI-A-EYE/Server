package kr.co.aeye.apiserver.oauth.controller;

import kr.co.aeye.apiserver.common.BaseResponse;
import kr.co.aeye.apiserver.common.BaseResponseStatus;
import kr.co.aeye.apiserver.jwt.tokens.TokenResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
public class OauthController {

    @GetMapping("/login")
    @ResponseBody
    public Map<String, String> login(@RequestParam(value = "auth") String access,
                                     @RequestParam(value = "refresh") String refresh) {
        Map<String, String> map = new HashMap<>();
        map.put("access", access);
        map.put("refresh", refresh);

        return map;
    }

    @GetMapping("/loginFail")
    @ResponseBody
    public Map<String, String> login(@RequestParam(value = "error") String error) {
        Map<String, String> map = new HashMap<>();
        map.put("error", error);

        return map;
    }

    @GetMapping("/login-success")
    public BaseResponse<TokenResponse> loginRedirect(
            @RequestParam(value = "access") String access,
            @RequestParam(value = "refresh") String refresh
    ) {
        TokenResponse tokenResponse = TokenResponse
                .builder()
                .accessToken(access)
                .refreshToken(refresh)
                .build();

        return new BaseResponse<>(BaseResponseStatus.SUCCESS, tokenResponse);
    }

}
