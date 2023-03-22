package kr.co.aeye.apiserver.api.child;

import kr.co.aeye.apiserver.api.child.dto.GetChildInfoRes;
import kr.co.aeye.apiserver.api.child.dto.PostChildReq;
import kr.co.aeye.apiserver.api.child.dto.PostChildRes;
import kr.co.aeye.apiserver.api.child.service.ChildService;
import kr.co.aeye.apiserver.common.BaseException;
import kr.co.aeye.apiserver.common.BaseResponse;
import kr.co.aeye.apiserver.common.BaseResponseStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/child")
public class ChildController {
    private final ChildService childService;

    @GetMapping("/advice")
    public BaseResponse<Map<String, ArrayList<GetChildInfoRes>>> listChild(){
        Map<String, ArrayList<GetChildInfoRes>> childrenList;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        try{
            childrenList = childService.getChildrenInfoList(authentication);
        } catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
        return new BaseResponse<>(BaseResponseStatus.SUCCESS, childrenList);
    }
    
    @PostMapping("/advice")
    public BaseResponse<PostChildRes> addChild(@RequestBody PostChildReq postChildReq){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PostChildRes postChildRes;
        try{
            postChildRes = childService.addChildInfo(authentication, postChildReq);
        } catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
        return new BaseResponse<>(BaseResponseStatus.SUCCESS, postChildRes);
    }

}
