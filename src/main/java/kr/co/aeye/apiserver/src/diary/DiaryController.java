package kr.co.aeye.apiserver.src.diary;

import kr.co.aeye.apiserver.config.BaseResponse;
import kr.co.aeye.apiserver.config.BaseResponseStatus;
import kr.co.aeye.apiserver.src.diary.model.Diary;
import kr.co.aeye.apiserver.src.diary.model.PostDiaryReq;
import kr.co.aeye.apiserver.src.diary.model.PostDiaryRes;
import kr.co.aeye.apiserver.src.diary.service.DiaryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/diary")
public class DiaryController {

    private final DiaryRepository diaryRepository;
    private final DiaryService diaryService;

    @PostMapping
    public BaseResponse<PostDiaryRes> addDiary(@RequestBody PostDiaryReq postDiaryReq){
        Diary newDiary = diaryService.addNewDiary(postDiaryReq);
        log.info("post new diary. new diary = {}", newDiary);

        PostDiaryRes postDiaryRes = PostDiaryRes.builder()
                .diaryId(newDiary.getId())
                .emotion(newDiary.getEmotion())
                .build();

        return new BaseResponse<>(BaseResponseStatus.CREATED, postDiaryRes);
    }
}
