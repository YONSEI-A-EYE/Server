package kr.co.aeye.apiserver.api.main;

import kr.co.aeye.apiserver.api.main.dto.MainResponse;
import kr.co.aeye.apiserver.common.BaseResponse;
import kr.co.aeye.apiserver.common.BaseResponseStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/")
public class MainController {
    @GetMapping
    public BaseResponse<MainResponse> getMainPage(){
        MainResponse mainResponse = MainResponse.builder()
                .message("hello world")
                .build();
        return new BaseResponse<>(BaseResponseStatus.SUCCESS, mainResponse);
    }
}
