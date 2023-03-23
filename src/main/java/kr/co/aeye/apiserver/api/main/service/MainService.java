package kr.co.aeye.apiserver.api.main.service;

import kr.co.aeye.apiserver.api.diary.entity.Diary;
import kr.co.aeye.apiserver.api.diary.entity.Video;
import kr.co.aeye.apiserver.api.diary.repository.DiaryRepository;
import kr.co.aeye.apiserver.api.main.dto.GetHomeRes;
import kr.co.aeye.apiserver.api.main.dto.HomeVideo;
import kr.co.aeye.apiserver.api.user.entity.Parent;
import kr.co.aeye.apiserver.api.user.entity.RoleType;
import kr.co.aeye.apiserver.api.user.entity.User;
import kr.co.aeye.apiserver.api.user.repository.ParentRepository;
import kr.co.aeye.apiserver.api.user.repository.UserRepository;
import kr.co.aeye.apiserver.common.BaseException;
import kr.co.aeye.apiserver.common.BaseResponseStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Slf4j
@Service
@RequiredArgsConstructor
public class MainService {
    private final UserRepository userRepository;
    private final DiaryRepository diaryRepository;
    private final ParentRepository parentRepository;


    public Parent getParentFromUserId(Long userId) throws BaseException {
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
        return parent;
    }


    public GetHomeRes getHomePage(Authentication authentication) throws BaseException{
        Long userId = Long.parseLong(authentication.getName());
        User user = userRepository.getUserById(userId);

        if (user.equals(null)){
            throw new BaseException(BaseResponseStatus.USER_NOT_FOUND);
        }

        Parent parent;
        String code;
        try{
            parent = getParentFromUserId(userId);
            code = parent.getAuthCode();
        }catch (BaseException e){
            throw new BaseException(BaseResponseStatus.PARENT_NOT_FOUND);
        }

        GetHomeRes homeRes = GetHomeRes.builder()
                .code(code)
                .build();

        LocalDate now = LocalDate.now();
        Diary diary = diaryRepository.getDiaryByUserAndDate(parent.getMainParent(), now);

        if (diary == null) {
            homeRes.setHomeVideo(null);
        }else{
            Video video = diary.getVideo();
            HomeVideo homeVideo = HomeVideo.builder()
                    .videoUrl(video.getVideoUrl())
                    .title(video.getTitle())
                    .build();
            homeRes.setHomeVideo(homeVideo);
        }

        return homeRes;
    }
}
