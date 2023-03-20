package kr.co.aeye.apiserver.api.user.service;

import kr.co.aeye.apiserver.api.user.repository.UserRepository;
import kr.co.aeye.apiserver.auth.dto.PatchCodeReq;
import kr.co.aeye.apiserver.auth.dto.PatchCodeRes;
import kr.co.aeye.apiserver.common.BaseException;
import org.springframework.stereotype.Service;


@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

//    // set roleType
//    public PatchCodeRes setRoleType(PatchCodeReq patchCodeReq) throws BaseException {
//        String reqRole = patchCodeReq.getRole();
//        if (reqRole.equals("main")){
//
//        }
//
//    }
}
