package kr.co.aeye.apiserver.src.diary.service;

import kr.co.aeye.apiserver.config.BaseException;
import kr.co.aeye.apiserver.config.BaseResponseStatus;
import kr.co.aeye.apiserver.src.diary.DiaryRepository;
import kr.co.aeye.apiserver.src.diary.model.*;
import kr.co.aeye.apiserver.src.user.UserRepository;
import kr.co.aeye.apiserver.src.user.models.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

import com.google.cloud.language.v1.Document;
import com.google.cloud.language.v1.Document.Type;
import com.google.cloud.language.v1.LanguageServiceClient;
import com.google.cloud.language.v1.Sentiment;

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

    // diary 조회하기 - emotion 저장 안된 diary
    public GetTempDiaryRes getDiaryById(int diaryId) throws BaseException{
        Optional<Diary> reqDiary = diaryRepository.findById(diaryId);
        if (reqDiary.isEmpty()){
            throw new BaseException(BaseResponseStatus.DIARY_NOT_FOUND);
        }
        Diary currDiary = reqDiary.get();

        String tempEmotion = getTempEmotionFromScoreMagnitude(currDiary.getScore(), currDiary.getMagnitude());
        log.info("tempEmotion={}", tempEmotion);

        return GetTempDiaryRes.builder()
                .content(reqDiary.get().getContent())
                .tempEmotion(tempEmotion)
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

        return result;
    }

    // diary 추가하기
    public PostDiaryRes addNewDiary(PostDiaryReq postDiaryReq) throws BaseException {
        int year = postDiaryReq.getYear();
        int month = postDiaryReq.getMonth();
        int day = postDiaryReq.getDay();

        LocalDate postDate = LocalDate.of(year, month, day);

        boolean isExist = diaryRepository.existsDiaryByUserAndDate(user, postDate);
        if (diaryRepository.existsDiaryByUserAndDate(user, postDate)){
            throw new BaseException(BaseResponseStatus.DIARY_ALREADY_EXIST);
        }

        // get sentiment from google api
        Sentiment sentiment = getEmotionFromGoogleCloud(postDiaryReq.getContent());
        String tempEmotion = getTempEmotionFromScoreMagnitude(sentiment.getScore(), sentiment.getMagnitude());
        log.info("tempEmotion={}", tempEmotion);

        // save new Diary
        Diary newDiary = Diary.builder()
                .date(postDate)
                .user(user)
                .content(postDiaryReq.getContent())
                .score(sentiment.getScore())
                .magnitude(sentiment.getMagnitude())
                .build();

        diaryRepository.save(newDiary);
        log.info("post new diary. new diary = {}", newDiary);

        return PostDiaryRes.builder()
                .diaryId(newDiary.getId())
                .tempEmotion(tempEmotion)
                .build();
    }

    // diary 수정하기
    public void updateDiaryService(int diaryId, UpdateDiaryReq updateDiaryReq) throws BaseException {
        Optional<Diary> reqDiary = diaryRepository.findById(diaryId);
        if (reqDiary.isEmpty()){
            throw new BaseException(BaseResponseStatus.DIARY_NOT_FOUND);
        }
        Diary currDiary = reqDiary.get();

        currDiary.setContent(updateDiaryReq.getContent());
        currDiary.setEmotion(updateDiaryReq.getEmotion());

        diaryRepository.save(currDiary);
    }

    private Sentiment getEmotionFromGoogleCloud (String content) throws RuntimeException{
        /**
         * get sentiment(score, magnitude) from google cloud api
         */
        // Instantiates a client
        LanguageServiceClient language;
        try {
            language = LanguageServiceClient.create();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // The text to analyze
        Document doc = Document.newBuilder().setContent(content).setType(Type.PLAIN_TEXT).build();

        // Detects the sentiment of the text
        return language.analyzeSentiment(doc).getDocumentSentiment();
    }

    private String getTempEmotionFromScoreMagnitude(float score, float magnitude) {
        /**
         * convert score, magnitude to emotion
         */
        if (score > 0.5) {
            if (magnitude >= 0.75) {
                return "excited";
            } else if (magnitude > 0.5) {
                return "happy";
            } else if (magnitude > 0.25) {
                return "content";
            } else {
                return "relaxed";
            }
        } else if (score > 0.1) {
            if (magnitude > 0.25) {
                return "goodSurprised";
            } else {
                return "calm";
            }
        } else if (score >= -0.1) {
            return "anticipate";
        } else if (score >= -0.5) {
            if (magnitude > 0.25) {
                return "badSurprised";
            } else {
                return "tired";
            }
        } else {
            if (magnitude >= 0.75) {
                return "tense";
            } else if (magnitude > 0.5) {
                return "angry";
            } else if (magnitude > 0.25) {
                return "sad";
            } else {
                return "bored";
            }
        }
    }
}
