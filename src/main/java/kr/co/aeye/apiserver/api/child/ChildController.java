package kr.co.aeye.apiserver.api.child;

import com.fasterxml.jackson.core.JsonProcessingException;
import kr.co.aeye.apiserver.api.child.dto.*;
import kr.co.aeye.apiserver.api.child.dto.bard.PostAdviceBardReq;
import kr.co.aeye.apiserver.api.child.dto.bard.SolutionObjectDto;
import kr.co.aeye.apiserver.api.child.service.BardAdviceService;
import kr.co.aeye.apiserver.api.child.service.ChildService;
import kr.co.aeye.apiserver.common.BaseException;
import kr.co.aeye.apiserver.common.BaseResponse;
import kr.co.aeye.apiserver.common.BaseResponseStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.parser.ParseException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/child")
public class ChildController {
    private final ChildService childService;
    private final BardAdviceService bardAdviceService;

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

    @PatchMapping("/advice/{childId}")
    public BaseResponse patchChild(@PathVariable Long childId, @RequestBody PostChildReq postChildReq){
        try{
            childService.updateChildInfo(postChildReq, childId);
        } catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
        return new BaseResponse<>(BaseResponseStatus.SUCCESS);
    }

    @GetMapping("/advice/bard")
    public BaseResponse<GetAdviceBardRes> getBardAdvice(@RequestParam Long childId){
        GetAdviceBardRes getAdviceBardRes;
        try{
            getAdviceBardRes = childService.getChildInfo(childId);
        } catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
        return new BaseResponse<>(BaseResponseStatus.SUCCESS, getAdviceBardRes);
    }

    @PostMapping("/advice/bard")
    public BaseResponse<List<SolutionObjectDto>> postBardAdvice(@RequestBody PostAdviceBardReq postAdviceBardReq){
        List<SolutionObjectDto> bardSolutionList;
        try{
            bardSolutionList = bardAdviceService.postAdviceToBard(postAdviceBardReq);
        } catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        } catch (ProtocolException e) {
            throw new RuntimeException(e);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return new BaseResponse<>(BaseResponseStatus.SUCCESS, bardSolutionList);
    }
}
