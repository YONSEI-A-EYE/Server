package kr.co.aeye.apiserver.api.diary.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Getter
@Builder
@Data
public class ResultDiaryRes {
    private int year;
    private int month;
    private int day;
    private String emotion;
    private String emotionText;
    private Float sentimentLevel;
    private String title;
    private String videoUrl;
}
