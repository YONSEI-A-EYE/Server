package kr.co.aeye.apiserver.auth.service;


import kr.co.aeye.apiserver.api.user.entity.User;
import kr.co.aeye.apiserver.api.user.repository.UserRepository;
import kr.co.aeye.apiserver.auth.dto.PostLoginReq;
import kr.co.aeye.apiserver.auth.dto.PostLoginRes;
import kr.co.aeye.apiserver.common.BaseException;
import kr.co.aeye.apiserver.common.BaseResponseStatus;
import kr.co.aeye.apiserver.jwt.tokens.TokenDto;
import kr.co.aeye.apiserver.jwt.tokens.TokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static org.springframework.http.HttpHeaders.SET_COOKIE;

@Slf4j
@Service
public class AuthService {

    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;

    public AuthService(UserRepository userRepository, TokenProvider tokenProvider){
        this.userRepository = userRepository;
        this.tokenProvider = tokenProvider;
    }

    // login
    public ResponseEntity login(PostLoginReq postLoginReq) throws BaseException{
        Optional<User> tempReqUser = userRepository.findByEmail(postLoginReq.getEmail());
        if (tempReqUser.isEmpty()){
            throw new BaseException(BaseResponseStatus.WRONG_EMAIL);
        }
        User reqUser = tempReqUser.get();

        if (!reqUser.getPassword().equals(postLoginReq.getPassword())){
            throw new BaseException(BaseResponseStatus.WRONG_PASSWORD);
        }
        TokenDto tokenDto = tokenProvider.generateTokenDto(reqUser);
        PostLoginRes postLoginRes = PostLoginRes.builder()
                .accessToken("Bearer " + tokenDto.getAccessToken())
                .build();

        return ResponseEntity.ok()
                .header(SET_COOKIE, tokenDto.getRefreshToken())
                .body(postLoginRes);
    }
}
