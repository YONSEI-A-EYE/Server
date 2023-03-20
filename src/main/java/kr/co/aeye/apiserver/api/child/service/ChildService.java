package kr.co.aeye.apiserver.api.child.service;

import kr.co.aeye.apiserver.api.child.dto.GetChildInfoRes;
import kr.co.aeye.apiserver.api.child.entity.Child;
import kr.co.aeye.apiserver.api.child.repository.ChildRepository;
import kr.co.aeye.apiserver.api.user.entity.Parent;
import kr.co.aeye.apiserver.api.user.entity.RoleType;
import kr.co.aeye.apiserver.api.user.entity.User;
import kr.co.aeye.apiserver.api.user.repository.ParentRepository;
import kr.co.aeye.apiserver.api.user.repository.UserRepository;
import kr.co.aeye.apiserver.common.BaseException;
import kr.co.aeye.apiserver.common.BaseResponseStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class ChildService {

    private final ChildRepository childRepository;
    private final ParentRepository parentRepository;
    private final UserRepository userRepository;

    public Map<String, ArrayList<GetChildInfoRes>> getChildrenInfoList(Authentication authentication) throws BaseException{
        Long userId = Long.parseLong(authentication.getName());
        User user = userRepository.getUserById(userId);
        RoleType roleType = user.getRoleType();
        Parent parent;
        if (roleType.equals(RoleType.MAIN_PARENT)) {
            parent = parentRepository.getParentByMainParent(userId);
        } else if (roleType.equals(RoleType.SUB_PARENT)){
            parent = parentRepository.getParentBySubParent(userId);
        } else{
            throw new BaseException(BaseResponseStatus.PARENT_NOT_FOUND);
        }
        if (parent.equals(null)){
            throw new BaseException(BaseResponseStatus.PARENT_NOT_FOUND);
        }

        List<Child> ChildrenListInfo = childRepository.getChildrenByParent(parent);
        ArrayList<GetChildInfoRes> ChildrenListData = new ArrayList<>();

        for (Child child: ChildrenListInfo){
            GetChildInfoRes newGetChildInfo = GetChildInfoRes.builder()
                    .id(child.getId())
                    .name(child.getChildName())
                    .temperament(child.getChildTemperament())
                    .build();
            ChildrenListData.add(newGetChildInfo);
        }
        Map<String, ArrayList<GetChildInfoRes>> childrenList = new HashMap<>();
        childrenList.put("children", ChildrenListData);

        return childrenList;
    }
}
