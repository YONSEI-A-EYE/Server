package kr.co.aeye.apiserver.auth.controller;

import kr.co.aeye.apiserver.auth.dto.PostLoginFailRes;
import kr.co.aeye.apiserver.auth.dto.PostLoginReq;
import kr.co.aeye.apiserver.auth.service.AuthService;
import kr.co.aeye.apiserver.common.BaseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody PostLoginReq postLoginReq) {
        ResponseEntity loginResponse;
        try {
            loginResponse = authService.login(postLoginReq);
        } catch (BaseException e){
            PostLoginFailRes postLoginFailRes = PostLoginFailRes.builder()
                    .success(false)
                    .message(e.getStatus().toString())
                    .build();
            return ResponseEntity.status(401).body(postLoginFailRes);
        }

        return loginResponse;
    }


}
