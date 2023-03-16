package kr.co.aeye.apiserver.auth.controller;

import kr.co.aeye.apiserver.api.user.dto.PostUserSignup;
import kr.co.aeye.apiserver.api.user.entity.User;
import kr.co.aeye.apiserver.auth.dto.PostLoginFailRes;
import kr.co.aeye.apiserver.auth.dto.PostLoginReq;
import kr.co.aeye.apiserver.auth.service.AuthService;
import kr.co.aeye.apiserver.common.BaseException;
import kr.co.aeye.apiserver.common.BaseResponse;
import kr.co.aeye.apiserver.common.BaseResponseStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


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

    @PostMapping("/signup")
    public BaseResponse<Boolean> signUp (@RequestBody PostUserSignup postUserSignup){
        Boolean isSuccess;
        try {
            isSuccess = authService.signUp(postUserSignup);
        } catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }

        return new BaseResponse<>(BaseResponseStatus.CREATED, isSuccess);
    }
}
