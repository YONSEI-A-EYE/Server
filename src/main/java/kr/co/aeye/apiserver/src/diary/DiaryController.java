package kr.co.aeye.apiserver.src.diary;

import kr.co.aeye.apiserver.config.BaseException;
import kr.co.aeye.apiserver.config.BaseResponse;
import kr.co.aeye.apiserver.config.BaseResponseStatus;
import kr.co.aeye.apiserver.src.diary.service.DiaryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import kr.co.aeye.apiserver.src.diary.model.*;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
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


    // 감정일기 수정페이지 조회하기
    @GetMapping("/{diaryId}")
    public BaseResponse<GetUpdateDiaryRes> getDiary(@PathVariable int diaryId) {
        GetUpdateDiaryRes updateDiaryRes;
        try {
            updateDiaryRes = diaryService.getDiaryById(diaryId);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }

        return new BaseResponse<>(BaseResponseStatus.SUCCESS, updateDiaryRes);
    }

    // 감정일기 수정페이지 수정하기
    @PatchMapping("/{diaryId}")
    public BaseResponse<UpdateDiaryRes> updateDiary(
            @PathVariable int diaryId,
            @RequestParam String type,
            @RequestBody UpdateDiaryReq updateDiaryReq){
        UpdateDiaryRes updateDiaryres;
        try{
            updateDiaryres = diaryService.updateDiaryService(diaryId, type, updateDiaryReq);
        } catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
        return new BaseResponse<>(BaseResponseStatus.SUCCESS, updateDiaryres);

    }

    @PostMapping
    public BaseResponse<PostDiaryRes> addDiary(@RequestBody PostDiaryReq postDiaryReq) {
        PostDiaryRes postDiaryRes;
        try {
            postDiaryRes = diaryService.addNewDiary(postDiaryReq);
        } catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }

        return new BaseResponse<>(BaseResponseStatus.CREATED, postDiaryRes);
    }

}
