package kr.co.aeye.apiserver.api.user;

import kr.co.aeye.apiserver.api.user.repository.UserRepository;
import kr.co.aeye.apiserver.auth.dto.PatchCodeReq;
import kr.co.aeye.apiserver.auth.dto.PatchCodeRes;
import kr.co.aeye.apiserver.common.BaseResponse;
import kr.co.aeye.apiserver.common.BaseResponseStatus;
import kr.co.aeye.apiserver.api.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserRepository userRepository;

    @GetMapping
    public BaseResponse<List<User>> getAllUser(){
        List<User> users = userRepository.findAll();

        log.info("find all users. users={}", users);
        return new BaseResponse<>(BaseResponseStatus.SUCCESS, users);
    }

    @GetMapping("/{userId}")
    public BaseResponse<Optional<User>> getUserById(@PathVariable int userId){
        Optional<User> reqUser = userRepository.findById(userId);
        if (reqUser.isEmpty()){
            log.info("No user id={}", userId);
            return new BaseResponse<>(BaseResponseStatus.BAD_REQUEST);
        }
        else {
            log.info("find user by id. reqUser={}", reqUser);
            return new BaseResponse<>(BaseResponseStatus.SUCCESS, reqUser);
        }
    }

    @GetMapping("/user/code")
//    public BaseResponse<PatchCodeRes> setRoleType(@RequestBody PatchCodeReq patchCodeReq){
    public void setRoleType(@RequestBody PatchCodeReq patchCodeReq){
        PatchCodeRes patchCodeRes;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        try{
//            patchCodeRes = authService.
//        }
    }
}
