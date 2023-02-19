package kr.co.aeye.apiserver.src.diary.model;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Getter
@Builder
@Data
public class ResultDiaryRes {
    private Date date;
    private String emotion;
    private String emotionText;
    private Float sentimentLevel;
    private String recommend;
}

@Getter
@Builder
@Data
public class Date {
    private int year;
    private int month;
    private int day;
}
