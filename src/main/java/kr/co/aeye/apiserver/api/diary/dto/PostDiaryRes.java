package kr.co.aeye.apiserver.api.diary.dto;

import lombok.*;

@Getter
@Builder
@Data
public class PostDiaryRes {
    private long diaryId;
    private String tempEmotion;
}
