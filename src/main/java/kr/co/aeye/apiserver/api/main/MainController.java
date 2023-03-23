package kr.co.aeye.apiserver.api.main;

import kr.co.aeye.apiserver.api.main.dto.GetHomeRes;
import kr.co.aeye.apiserver.api.main.dto.MainResponse;
import kr.co.aeye.apiserver.api.main.service.MainService;
import kr.co.aeye.apiserver.common.BaseException;
import kr.co.aeye.apiserver.common.BaseResponse;
import kr.co.aeye.apiserver.common.BaseResponseStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping
public class MainController {
    private final MainService mainService;
    @GetMapping
    public BaseResponse<MainResponse> getMainPage(){
        MainResponse mainResponse = MainResponse.builder()
                .message("hello world")
                .build();
        return new BaseResponse<>(BaseResponseStatus.SUCCESS, mainResponse);
    }

    @GetMapping("/home")
    public BaseResponse<GetHomeRes> getHomePage(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        GetHomeRes getHomeRes;
        try{
            getHomeRes = mainService.getHomePage(authentication);
        } catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
        return new BaseResponse<>(BaseResponseStatus.SUCCESS, getHomeRes);
    }
}
