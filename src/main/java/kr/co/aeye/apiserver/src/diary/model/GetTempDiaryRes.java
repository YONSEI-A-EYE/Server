package kr.co.aeye.apiserver.src.diary.model;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Getter
@Builder
@Data
public class GetTempDiaryRes {
    private String content;
    private String tempEmotion;
}
