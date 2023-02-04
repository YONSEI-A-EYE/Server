package kr.co.aeye.apiserver.src.diary.model;

import lombok.*;

@Getter
@Builder
@Data
public class PostDiaryRes {
    private long diaryId;
    private String tempEmotion;
}
