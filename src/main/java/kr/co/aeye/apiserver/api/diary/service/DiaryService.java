package kr.co.aeye.apiserver.api.diary.service;

import kr.co.aeye.apiserver.api.diary.dto.*;
import kr.co.aeye.apiserver.api.diary.entity.Diary;
import kr.co.aeye.apiserver.api.diary.entity.Video;
import kr.co.aeye.apiserver.common.BaseException;
import kr.co.aeye.apiserver.common.BaseResponseStatus;
import kr.co.aeye.apiserver.api.diary.dto.diaryReport.EmotionHistogram;
import kr.co.aeye.apiserver.api.diary.dto.diaryReport.MonthlyEmotion;
import kr.co.aeye.apiserver.api.diary.dto.diaryReport.SentimentLevel;
import kr.co.aeye.apiserver.api.diary.repository.DiaryRepository;
import kr.co.aeye.apiserver.api.diary.repository.VideoRepository;
import kr.co.aeye.apiserver.api.diary.utils.GetEmotionResponse;
import kr.co.aeye.apiserver.api.diary.utils.GetMonthlySentimentLevel;
import kr.co.aeye.apiserver.api.diary.utils.GetSentimentLevel;
import kr.co.aeye.apiserver.api.diary.utils.GoogleEmotion;
import kr.co.aeye.apiserver.api.user.repository.UserRepository;
import kr.co.aeye.apiserver.api.user.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.*;
import com.google.cloud.language.v1.Sentiment;

@Slf4j
@Service
public class DiaryService {

    private final DiaryRepository diaryRepository;
    private final VideoRepository videoRepository;
    private final UserRepository userRepository;

    public DiaryService(DiaryRepository diaryRepository, VideoRepository videoRepository, UserRepository userRepository){
        this.diaryRepository = diaryRepository;
        this.videoRepository = videoRepository;
        this.userRepository = userRepository;
    }

    // diary 조회하기 - emotion 저장 안된 diary
    public GetUpdateDiaryRes getDiaryById(Long diaryId) throws BaseException{
        Optional<Diary> reqDiary = diaryRepository.findById(diaryId);
        if (reqDiary.isEmpty()){
            throw new BaseException(BaseResponseStatus.DIARY_NOT_FOUND);
        }
        Diary currDiary = reqDiary.get();

        String tempEmotion = GoogleEmotion.getTempEmotionFromScoreMagnitude(currDiary.getScore(), currDiary.getMagnitude());
        String emotion = currDiary.getEmotion();
        log.info("retrieve diary edit page. diaryId={}", currDiary.getId());

        return GetUpdateDiaryRes.builder()
                .content(reqDiary.get().getContent())
                .tempEmotion(tempEmotion)
                .emotion(emotion)
                .build();
    }

    // 월별 diary 조회하기
    public TreeMap<Integer, GetMonthlyDiaryRes> getMonthlyDiary(int year, int month){
        LocalDate firstDate = LocalDate.of(year, month, 1);
        LocalDate lastDate = firstDate.withDayOfMonth(firstDate.lengthOfMonth());

        List<Diary> diaryList = diaryRepository.findDiariesByDateBetween(firstDate, lastDate);
        HashMap<Integer, Diary> diaryHashMap = new HashMap<>();
        for (Diary diary : diaryList) {
            int diaryDay = diary.getDate().getDayOfMonth();
            diaryHashMap.put(diaryDay, diary);
        }

        HashMap<Integer, GetMonthlyDiaryRes> resHashMap = new HashMap<>();
        for (int i = 1; i <= firstDate.lengthOfMonth(); i++){
            Diary diary = diaryHashMap.get(i);
            GetMonthlyDiaryRes diaryRes;
            if (diary == null){
                diaryRes = GetMonthlyDiaryRes.builder()
                        .id(null)
                        .emotion(null)
                        .content(null)
                        .build();
            }
            else{
                 diaryRes = GetMonthlyDiaryRes.builder()
                        .id(diary.getId())
                        .emotion(diary.getEmotion())
                        .content(diary.getContent())
                        .build();
            }
            resHashMap.put(i, diaryRes);
        }
        TreeMap<Integer, GetMonthlyDiaryRes> result = new TreeMap<>(resHashMap);
        log.info("retrieve {} diary edit page.", year + "/" + month);

        return result;
    }

    // diary 추가하기
    public PostDiaryRes addNewDiary(PostDiaryReq postDiaryReq) throws BaseException {
        int year = postDiaryReq.getYear();
        int month = postDiaryReq.getMonth();
        int day = postDiaryReq.getDay();

        LocalDate postDate = LocalDate.of(year, month, day);

        Optional<User> tempUser = userRepository.findById(1);
        if (tempUser.isEmpty()){
            throw new BaseException(BaseResponseStatus.USER_NOT_FOUND);
        }
        User user = tempUser.get();

        boolean isExist = diaryRepository.existsDiaryByUserAndDate(user, postDate);
        if (isExist){
            throw new BaseException(BaseResponseStatus.DIARY_ALREADY_EXIST);
        }

        // get sentiment from google api
        Sentiment sentiment = GoogleEmotion.getEmotionFromGoogleCloud(postDiaryReq.getContent());
        String tempEmotion = GoogleEmotion.getTempEmotionFromScoreMagnitude(sentiment.getScore(), sentiment.getMagnitude());
        log.info("tempEmotion={}", tempEmotion);
        String theme = GetEmotionResponse.getThemeFromEmotion(tempEmotion);
        Video newVideo = videoRepository.findVideoRandByTheme(theme);
        log.info("newVideo={}", newVideo.getTitle());

        // save new Diary
        Diary newDiary = Diary.builder()
                .date(postDate)
                .user(user)
                .content(postDiaryReq.getContent())
                .score(sentiment.getScore())
                .magnitude(sentiment.getMagnitude())
                .video(newVideo)
                .build();

        diaryRepository.save(newDiary);
        log.info("post new diary. new diary id = {}", newDiary.getId());

        return PostDiaryRes.builder()
                .diaryId(newDiary.getId())
                .tempEmotion(tempEmotion)
                .build();
    }

    // diary 수정하기
    public UpdateDiaryRes updateDiaryService(Long diaryId, String type, UpdateDiaryReq updateDiaryReq) throws BaseException {
        Optional<Diary> reqDiary = diaryRepository.findById(diaryId);
        if (reqDiary.isEmpty()){
            log.error("update fail. wrong diary id. diary id = {}", diaryId);
            throw new BaseException(BaseResponseStatus.DIARY_NOT_FOUND);
        }
        Diary currDiary = reqDiary.get();

        String tempEmotion;
        UpdateDiaryRes res;

        switch (type){
            case "content":
                // get content
                String content = updateDiaryReq.getContent();
                if (content == null){
                    throw new BaseException(BaseResponseStatus.BAD_REQUEST);
                }

                // get sentiment from google api
                Sentiment sentiment = GoogleEmotion.getEmotionFromGoogleCloud(content);
                tempEmotion = GoogleEmotion.getTempEmotionFromScoreMagnitude(sentiment.getScore(), sentiment.getMagnitude());
                log.info("tempEmotion={}", tempEmotion);

                // set video
                String theme = GetEmotionResponse.getThemeFromEmotion(tempEmotion);
                Video newVideo = videoRepository.findVideoRandByTheme(theme);
                log.info("newVideo={}", newVideo);

                // update content
                currDiary.setContent(content);
                currDiary.setEmotion(null);
                currDiary.setScore(sentiment.getScore());
                currDiary.setMagnitude(sentiment.getMagnitude());
                currDiary.setVideo(newVideo);

                res = UpdateDiaryRes.builder().tempEmotion(tempEmotion).build();
                break;
            case "emotion":
                String emotion = updateDiaryReq.getEmotion();
                currDiary.setEmotion(emotion);
                res = null;
                break;
            default:
                throw new BaseException(BaseResponseStatus.BAD_REQUEST);
        }

        diaryRepository.save(currDiary);
        log.info("update diary. diary id = {}", currDiary.getId());

        return res;
    }

    // diary result 페이지
    public ResultDiaryRes getDiaryResultService(Long diaryId) throws BaseException {
        Optional<Diary> reqDiary = diaryRepository.findById(diaryId);
        if (reqDiary.isEmpty()){
            log.error("fail to find retrieve diary. diary id = {}", diaryId);
            throw new BaseException(BaseResponseStatus.DIARY_NOT_FOUND);
        }
        Diary currDiary = reqDiary.get();
        LocalDate diaryDate = currDiary.getDate();
        String emotion = currDiary.getEmotion();
        String emotionText = GetEmotionResponse.getEmotionResponse(emotion);

        float sentiment = GetSentimentLevel.getSentimentLevel(currDiary.getScore(), currDiary.getMagnitude());

        if (currDiary.getVideo() == null){
            String tempEmotion = GoogleEmotion.getTempEmotionFromScoreMagnitude(currDiary.getScore(), currDiary.getMagnitude());
            String theme = GetEmotionResponse.getThemeFromEmotion(tempEmotion);
            Video newVideo = videoRepository.findVideoRandByTheme(theme);
            currDiary.setVideo(newVideo);
            diaryRepository.save(currDiary);
            log.info("save new Video during retrieve result. diaryId={}", currDiary.getId());
        }

        ResultDiaryRes res = ResultDiaryRes
                .builder()
                .year(diaryDate.getYear())
                .month(diaryDate.getMonthValue())
                .day(diaryDate.getDayOfMonth())
                .emotion(emotion)
                .emotionText(emotionText)
                .sentimentLevel(sentiment)
                .title(currDiary.getVideo().getTitle())
                .videoUrl(currDiary.getVideo().getVideoUrl())
                .build();

        return res;
    }

    //diary monthly report 페이지
    public GetMonthlyReportRes getDiaryMonthlyReportService(int year, int month) throws BaseException{
        LocalDate firstDate = LocalDate.of(year, month, 1);
        LocalDate lastDate = firstDate.withDayOfMonth(firstDate.lengthOfMonth());

        EmotionHistogram emotionHistogram = diaryRepository.getEmotionHistogramByDateBetween(firstDate, lastDate);
        log.info("emotionHistogram ={}", emotionHistogram);
        List<Diary> diaryList = diaryRepository.findDiariesByDateBetween(firstDate, lastDate);
        log.info("diaryList ={}", diaryList);
        SentimentLevel sentimentLevel = GetMonthlySentimentLevel.getMonthlySentimentLevel(diaryList);
        String mostFrequentEmotion = emotionHistogram.getMostFrequentEmotion();
        log.info("emotionHistogram ={}", emotionHistogram);
        String emotionText = GetEmotionResponse.getEmotionResponse(mostFrequentEmotion);
        MonthlyEmotion monthlyEmotion = MonthlyEmotion.builder().emotion(mostFrequentEmotion).comment(emotionText).build();

        return GetMonthlyReportRes
                .builder()
                .sentimentLevel(sentimentLevel)
                .emotionHistogram(emotionHistogram)
                .monthlyEmotion(monthlyEmotion)
                .build();
    }
}
