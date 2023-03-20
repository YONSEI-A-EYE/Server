package kr.co.aeye.apiserver.api.user.service;

import kr.co.aeye.apiserver.api.user.entity.Parent;
import kr.co.aeye.apiserver.api.user.entity.RoleType;
import kr.co.aeye.apiserver.api.user.entity.User;
import kr.co.aeye.apiserver.api.user.repository.ParentRepository;
import kr.co.aeye.apiserver.api.user.repository.UserRepository;
import kr.co.aeye.apiserver.api.user.utils.AuthCodeGenerator;
import kr.co.aeye.apiserver.auth.dto.PatchCodeReq;
import kr.co.aeye.apiserver.auth.dto.PatchCodeRes;
import kr.co.aeye.apiserver.common.BaseException;
import kr.co.aeye.apiserver.common.BaseResponseStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ParentRepository parentRepository;


    // set roleType
    public PatchCodeRes setRoleType(PatchCodeReq patchCodeReq, Authentication authentication) throws BaseException {
        PatchCodeRes patchCodeRes;
        String reqRole = patchCodeReq.getRole();
        Long userId = Long.parseLong(authentication.getName());
        User user = userRepository.getUserById(userId);
        RoleType roleType = user.getRoleType();

        if (!roleType.equals(RoleType.USER)){
            throw new BaseException(BaseResponseStatus.BAD_REQUEST);
        }

        if (reqRole.equals("main")){
            user.setRoleType(RoleType.MAIN_PARENT);
            String newAuthCode = AuthCodeGenerator.getAuthCode();
            Parent newParent = Parent.builder()
                    .authCode(newAuthCode)
                    .mainParent(user)
                    .build();

            parentRepository.save(newParent);
            log.info("new parent {}", newParent);

            patchCodeRes = PatchCodeRes.builder()
                    .code(newAuthCode).build();
        }else {
            String authCode = patchCodeReq.getCode();
            Parent parent = parentRepository.getParentByAuthCode(authCode);
            if (parent.equals(null)){
                throw new BaseException(BaseResponseStatus.WRONG_PARENT_CODE);
            }
            user.setRoleType(RoleType.SUB_PARENT);
            parent.setSubParent(user);
            patchCodeRes = PatchCodeRes.builder().build();
            parentRepository.save(parent);
        }
        userRepository.save(user);
        return patchCodeRes;
    }
}
