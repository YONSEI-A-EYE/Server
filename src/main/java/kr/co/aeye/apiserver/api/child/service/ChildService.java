package kr.co.aeye.apiserver.api.child.service;

import kr.co.aeye.apiserver.api.child.dto.*;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
@Service
@RequiredArgsConstructor
public class ChildService {

    private final ChildRepository childRepository;
    private final ParentRepository parentRepository;
    private final UserRepository userRepository;

    public Parent getParentFromUserId(Long userId) throws BaseException {
        User user = userRepository.getUserById(userId);
        RoleType roleType = user.getRoleType();
        Parent parent;
        if (roleType.equals(RoleType.MAIN_PARENT)) {
            parent = parentRepository.getParentByMainParent(user);
        } else if (roleType.equals(RoleType.SUB_PARENT)) {
            parent = parentRepository.getParentBySubParent(user);
        } else {
            throw new BaseException(BaseResponseStatus.PARENT_NOT_FOUND);
        }
        if (parent.equals(null)) {
            throw new BaseException(BaseResponseStatus.PARENT_NOT_FOUND);
        }
        return parent;
    }

    public Map<String, ArrayList<GetChildInfoRes>> getChildrenInfoList(Authentication authentication) throws BaseException{
        Long userId = Long.parseLong(authentication.getName());
        Parent parent = getParentFromUserId(userId);

        List<Child> ChildrenListInfo = childRepository.getChildrenByParent(parent);
        ArrayList<GetChildInfoRes> ChildrenListData = new ArrayList<>();

        for (Child child: ChildrenListInfo){
            GetChildInfoRes newGetChildInfo = GetChildInfoRes.builder()
                    .id(child.getId())
                    .name(child.getChildName())
                    .age(child.getChildAge())
                    .temperament(child.getChildTemperament())
                    .build();
            ChildrenListData.add(newGetChildInfo);
        }
        Map<String, ArrayList<GetChildInfoRes>> childrenList = new HashMap<>();
        childrenList.put("children", ChildrenListData);

        return childrenList;
    }

    public PostChildRes addChildInfo(Authentication authentication, PostChildReq postChildReq) throws BaseException{
        Long userId = Long.parseLong(authentication.getName());
        Parent parent = getParentFromUserId(userId);

        Child newChild = Child.builder()
                .childName(postChildReq.getName())
                .childTemperament(postChildReq.getTemperament())
                .childAge(postChildReq.getAge())
                .parent(parent)
                .build();
        childRepository.save(newChild);
        log.info("save new child {} to parent {}", newChild.getId(), parent.getId());

        return PostChildRes.builder().childId(newChild.getId()).build();
    }

    public void updateChildInfo(PostChildReq postChildReq, Long childId) throws BaseException{
        Child child = childRepository.getChildById(childId);
        if (child.equals(null)){
            throw new BaseException(BaseResponseStatus.BAD_REQUEST);
        }
        child.setChildName(postChildReq.getName());
        child.setChildAge(postChildReq.getAge());
        child.setChildTemperament(postChildReq.getTemperament());
        childRepository.save(child);
        log.info("update child {}", child.getId());
    }

    public GetAdviceBardRes getChildInfo(Long childId) throws BaseException{
        Child child = childRepository.getChildById(childId);
        if (child.equals(null)){
            throw new BaseException(BaseResponseStatus.BAD_REQUEST);
        }
        GetAdviceBardRes getAdviceBardRes = GetAdviceBardRes.builder()
                .childName(child.getChildName())
                .childAge(child.getChildAge())
                .childTemperament(child.getChildTemperament())
                .build();

        return getAdviceBardRes;
    }
}
