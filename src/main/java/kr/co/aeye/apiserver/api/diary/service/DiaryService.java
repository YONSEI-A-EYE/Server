package kr.co.aeye.apiserver.api.diary.service;

import kr.co.aeye.apiserver.api.diary.dto.*;
import kr.co.aeye.apiserver.api.diary.entity.Comment;
import kr.co.aeye.apiserver.api.diary.entity.Diary;
import kr.co.aeye.apiserver.api.diary.entity.Video;
import kr.co.aeye.apiserver.api.diary.repository.CommentRepository;
import kr.co.aeye.apiserver.api.user.entity.Parent;
import kr.co.aeye.apiserver.api.user.entity.RoleType;
import kr.co.aeye.apiserver.api.user.repository.ParentRepository;
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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.*;
import com.google.cloud.language.v1.Sentiment;

@Slf4j
@Service
@RequiredArgsConstructor
public class DiaryService {

    private final DiaryRepository diaryRepository;
    private final VideoRepository videoRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final ParentRepository parentRepository;

    public User getMainParentUserFromUserId(Long userId) throws BaseException {
        User user = userRepository.getUserById(userId);
        RoleType roleType = user.getRoleType();
        Parent parent;
        if (roleType.equals(RoleType.MAIN_PARENT)) {
            parent = parentRepository.getParentByMainParent(user);
        } else if (roleType.equals(RoleType.SUB_PARENT)) {
            parent = parentRepository.getParentBySubParent(user);
        } else {
            throw new BaseException(BaseResponseStatus.PARENT_NOT_FOUND);
        }
        if (parent.equals(null)) {
            throw new BaseException(BaseResponseStatus.PARENT_NOT_FOUND);
        }
        return parent.getMainParent();
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
    public TreeMap<Integer, GetMonthlyDiaryRes> getMonthlyDiary(Authentication authentication, int year, int month) throws BaseException{
        Long userId = Long.parseLong(authentication.getName());
        User mainParentUser = getMainParentUserFromUserId(userId);

        LocalDate firstDate = LocalDate.of(year, month, 1);
        LocalDate lastDate = firstDate.withDayOfMonth(firstDate.lengthOfMonth());

        List<Diary> diaryList = diaryRepository.findDiariesByUserAndDateBetweenAndEmotionIsNotNull(mainParentUser, firstDate, lastDate);
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
    public PostDiaryRes addNewDiary(Authentication authentication, PostDiaryReq postDiaryReq) throws BaseException {
        Long userId = Long.parseLong(authentication.getName());
        User user = userRepository.getUserById(userId);
        if (user == null){
            throw new BaseException(BaseResponseStatus.USER_NOT_FOUND);
        }

        int year = postDiaryReq.getYear();
        int month = postDiaryReq.getMonth();
        int day = postDiaryReq.getDay();

        LocalDate postDate = LocalDate.of(year, month, day);

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
    public GetMonthlyReportRes getDiaryMonthlyReportService(Authentication authentication, int year, int month) throws BaseException{
        Long userId = Long.parseLong(authentication.getName());
        User mainParentUser = getMainParentUserFromUserId(userId);

        LocalDate firstDate = LocalDate.of(year, month, 1);
        LocalDate lastDate = firstDate.withDayOfMonth(firstDate.lengthOfMonth());

        EmotionHistogram emotionHistogram = diaryRepository.getEmotionHistogramByDateBetween(mainParentUser.getId(), firstDate, lastDate);
        log.info("emotionHistogram ={}", emotionHistogram);
        List<Diary> diaryList = diaryRepository.findDiariesByUserAndDateBetweenAndEmotionIsNotNull(mainParentUser, firstDate, lastDate);
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

    //댓글 작성
    public PostCommentRes postCommentService(Long diaryId, PostCommentReq postCommentReq, Authentication authentication) throws BaseException{
        Long userId = Long.parseLong(authentication.getName());
        User user = userRepository.getUserById(userId);
        if (user==null){
            throw new BaseException(BaseResponseStatus.USER_NOT_FOUND);
        }

        Diary reqDiary = diaryRepository.getById(diaryId);
        if (reqDiary == null){
            throw new BaseException(BaseResponseStatus.DIARY_NOT_FOUND);
        }

        Comment newComment = Comment.builder()
                .user(user)
                .content(postCommentReq.getCommentContent())
                .diary(reqDiary)
                .build();

        commentRepository.save(newComment);
        log.info("save new comment={}", newComment.getId());

        int commentOrder = commentRepository.countCommentsByDiary(reqDiary).intValue();
        Long date = Timestamp.valueOf(newComment.getCreatedAt()).getTime();
        RoleType roleType = user.getRoleType();
        String role = getRole(roleType);

        return PostCommentRes.builder()
                .order(commentOrder)
                .date(date)
                .role(role)
                .build();
    }

    // 댓글 조회
    public GetCommentRes getCommentPageService(Long diaryId) throws BaseException{
        Diary reqDiary = diaryRepository.getById(diaryId);
        if (reqDiary == null){
            throw new BaseException(BaseResponseStatus.DIARY_NOT_FOUND);
        }

        List<Comment> commentList = commentRepository.getAllByDiaryOrderById(reqDiary);
        List<CommentResult> commentResultList = new ArrayList<>();
        int idx = 1;
        for (Comment comment: commentList){
            String role = getRole(comment.getUser().getRoleType());
            Long date = Timestamp.valueOf(comment.getCreatedAt()).getTime();
            CommentResult commentResult = CommentResult.builder()
                    .order(idx)
                    .role(role)
                    .commentContent(comment.getContent())
                    .commentCreatedAt(date)
                    .build();
            commentResultList.add(commentResult);
            idx++;
        }


        return GetCommentRes.builder()
                .content(reqDiary.getContent())
                .emotion(reqDiary.getEmotion())
                .comments(commentResultList)
                .build();
    }

    public String getRole(RoleType roleType) throws BaseException{
        String role;
        if (roleType.equals(RoleType.MAIN_PARENT)) {
            role = "main";
        }else if (roleType.equals(RoleType.SUB_PARENT)){
            role = "sub";
        }else{
            throw new BaseException(BaseResponseStatus.PARENT_NOT_FOUND);
        }
        return role;
    }
}
