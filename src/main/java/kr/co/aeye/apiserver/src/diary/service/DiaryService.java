package kr.co.aeye.apiserver.src.diary.service;

import kr.co.aeye.apiserver.config.BaseException;
import kr.co.aeye.apiserver.config.BaseResponseStatus;
import kr.co.aeye.apiserver.src.diary.DiaryRepository;
import kr.co.aeye.apiserver.src.diary.model.Diary;
import kr.co.aeye.apiserver.src.diary.model.GetTempDiaryRes;
import kr.co.aeye.apiserver.src.diary.model.PostDiaryReq;
import kr.co.aeye.apiserver.src.diary.model.PostDiaryRes;
import kr.co.aeye.apiserver.src.user.UserRepository;
import kr.co.aeye.apiserver.src.user.models.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Slf4j
@Service
public class DiaryService {

    private final DiaryRepository diaryRepository;
    private final UserRepository userRepository;
    private User user;

    public DiaryService(DiaryRepository diaryRepository, UserRepository userRepository) throws BaseException{
        this.diaryRepository = diaryRepository;
        this.userRepository = userRepository;
        Optional<User> user = userRepository.findById(1);
        if (user.isEmpty()){
            throw new BaseException(BaseResponseStatus.USER_NOT_FOUND);
        }
        this.user = user.get();
    }

    // diary 조회하기
    public GetTempDiaryRes getDiaryById(int diaryId) throws BaseException{
        Optional<Diary> reqDiary = diaryRepository.findById(diaryId);
        if (reqDiary.isEmpty()){
            throw new BaseException(BaseResponseStatus.DIARY_NOT_FOUND);
        }

        String tempEmotion = null;

        return GetTempDiaryRes.builder()
                .content(reqDiary.get().getContent())
                .tempEmotion(tempEmotion)
                .build();
    }

    // diary 추가하기
    public PostDiaryRes addNewDiary(PostDiaryReq postDiaryReq){
        int year = postDiaryReq.getYear();
        int month = postDiaryReq.getMonth();
        int day = postDiaryReq.getDay();

        LocalDate postDate = LocalDate.of(year, month, day);

        Diary newDiary = Diary.builder()
                .date(postDate)
                .user(user)
                .content(postDiaryReq.getContent())
                .build();

        diaryRepository.save(newDiary);
        log.info("post new diary. new diary = {}", newDiary);

        String tempEmotion = null;

        return PostDiaryRes.builder()
                .diaryId(newDiary.getId())
                .tempEmotion(tempEmotion)
                .build();
    }
}
