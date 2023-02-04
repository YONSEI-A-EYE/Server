package kr.co.aeye.apiserver.src.diary;

import kr.co.aeye.apiserver.config.BaseException;
import kr.co.aeye.apiserver.config.BaseResponse;
import kr.co.aeye.apiserver.config.BaseResponseStatus;
import kr.co.aeye.apiserver.src.diary.model.*;
import kr.co.aeye.apiserver.src.diary.service.DiaryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/diary")
public class DiaryController {

    private final DiaryService diaryService;

    @GetMapping("/month")
    public BaseResponse<TreeMap<Integer, GetMonthlyDiaryRes>> getMonthlyDiary(@RequestParam int year, int month){
        TreeMap<Integer, GetMonthlyDiaryRes> monthlyDiary = diaryService.getMonthlyDiary(year, month);
        return new BaseResponse<>(BaseResponseStatus.SUCCESS, monthlyDiary);
    }


    @GetMapping("/{diaryId}")
    public BaseResponse<GetTempDiaryRes> getDiary(@PathVariable int diaryId) {
        GetTempDiaryRes tempDiaryRes;
        try {
            tempDiaryRes = diaryService.getDiaryById(diaryId);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }

        return new BaseResponse<>(BaseResponseStatus.SUCCESS, tempDiaryRes);
    }

    @PatchMapping("/{diaryId}")
    public BaseResponse<String> updateDiary(@PathVariable int diaryId, @RequestBody UpdateDiaryReq updateDiaryReq){
        try{
            diaryService.updateDiaryService(diaryId, updateDiaryReq);
        } catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
        return new BaseResponse<>(BaseResponseStatus.SUCCESS, "success");

    }

    @PostMapping
    public BaseResponse<PostDiaryRes> addDiary(@RequestBody PostDiaryReq postDiaryReq){
        PostDiaryRes postDiaryRes;
        try {
            postDiaryRes = diaryService.addNewDiary(postDiaryReq);
        } catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }

        return new BaseResponse<>(BaseResponseStatus.CREATED, postDiaryRes);
    }

}
