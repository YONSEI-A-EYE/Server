package kr.co.aeye.apiserver.api.diary;

import kr.co.aeye.apiserver.api.diary.dto.*;
import kr.co.aeye.apiserver.common.BaseException;
import kr.co.aeye.apiserver.common.BaseResponse;
import kr.co.aeye.apiserver.common.BaseResponseStatus;
import kr.co.aeye.apiserver.api.diary.service.DiaryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

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
    public BaseResponse<GetUpdateDiaryRes> getUpdateDiary(@PathVariable Long diaryId) {
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
            @PathVariable Long diaryId,
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

    // 감정일기 추가
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

    // 감정일기 결과 페이지
    @GetMapping("/result/{diaryId}")
    public BaseResponse<ResultDiaryRes> getResultDiary(@PathVariable Long diaryId){
        ResultDiaryRes resultDiaryRes;
        try{
            resultDiaryRes = diaryService.getDiaryResultService(diaryId);
        } catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }

        return new BaseResponse<>(BaseResponseStatus.SUCCESS, resultDiaryRes);
    }

    // 감정일기 월별 레포트 페이지
    @GetMapping("/report")
    public BaseResponse<GetMonthlyReportRes> getMonthlyReport(@RequestParam int year, int month){
        GetMonthlyReportRes getMonthlyReportRes;
        try{
            getMonthlyReportRes = diaryService.getDiaryMonthlyReportService(year, month);
        } catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }

        return new BaseResponse<>(BaseResponseStatus.SUCCESS, getMonthlyReportRes);
    }
}
