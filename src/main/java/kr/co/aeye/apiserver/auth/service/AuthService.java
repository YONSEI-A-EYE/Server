package kr.co.aeye.apiserver.auth.service;


import kr.co.aeye.apiserver.api.user.dto.PostUserSignup;
import kr.co.aeye.apiserver.api.user.entity.RoleType;
import kr.co.aeye.apiserver.api.user.entity.User;
import kr.co.aeye.apiserver.api.user.repository.UserRepository;
import kr.co.aeye.apiserver.auth.dto.PostLoginReq;
import kr.co.aeye.apiserver.auth.dto.PostLoginRes;
import kr.co.aeye.apiserver.common.BaseException;
import kr.co.aeye.apiserver.common.BaseResponseStatus;
import kr.co.aeye.apiserver.jwt.tokens.TokenDto;
import kr.co.aeye.apiserver.jwt.tokens.TokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static org.springframework.http.HttpHeaders.SET_COOKIE;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final PasswordEncoder passwordEncoder;


    // login
    public ResponseEntity login(PostLoginReq postLoginReq) throws BaseException{
        Authentication authentication;
        RoleType roleType = null;
        try{
            Optional<User> tempUser = userRepository.findByEmail(postLoginReq.getEmail());
            if (tempUser.isPresent()){
                User user = tempUser.get();
                roleType = user.getRoleType();
            }

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(postLoginReq.getEmail(), postLoginReq.getPassword());
            authenticationToken.setDetails(roleType);
            authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        } catch (BadCredentialsException e){
            throw new BaseException(BaseResponseStatus.WRONG_CREDENTIAL);
        }

        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);
        PostLoginRes postLoginRes = PostLoginRes.builder()
                .accessToken("Bearer " + tokenDto.getAccessToken())
                .build();

        return ResponseEntity.ok()
                .header(SET_COOKIE, tokenDto.getRefreshToken())
                .body(postLoginRes);
    }

    // signup
    public boolean signUp(PostUserSignup postUserSignup) throws BaseException{
        String reqEmail = postUserSignup.getEmail();
        Optional<User> reqUser = userRepository.findByEmail(reqEmail);
        if (postUserSignup.getEmail() == null || postUserSignup.getPassword() == null){
            throw new BaseException(BaseResponseStatus.BAD_REQUEST);
        }
        if (!reqUser.isEmpty()){
            throw new BaseException(BaseResponseStatus.ALREADY_EXIST_EMAIL);
        }

        String password = passwordEncoder.encode(postUserSignup.getPassword());
        User newUser = User.builder()
                .name(postUserSignup.getName())
                .email(postUserSignup.getEmail())
                .password(password)
                .roleType(RoleType.USER)
                .build();

        log.info("sign up new user. newUser={}", newUser);
        userRepository.save(newUser);
        return true;
    }
}
