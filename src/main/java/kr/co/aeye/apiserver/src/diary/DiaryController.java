package kr.co.aeye.apiserver.src.diary;

import kr.co.aeye.apiserver.config.BaseException;
import kr.co.aeye.apiserver.config.BaseResponse;
import kr.co.aeye.apiserver.config.BaseResponseStatus;
import kr.co.aeye.apiserver.src.diary.model.Diary;
import kr.co.aeye.apiserver.src.diary.model.GetTempDiaryRes;
import kr.co.aeye.apiserver.src.diary.model.PostDiaryReq;
import kr.co.aeye.apiserver.src.diary.model.PostDiaryRes;
import kr.co.aeye.apiserver.src.diary.service.DiaryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/diary")
public class DiaryController {

    private final DiaryService diaryService;


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
