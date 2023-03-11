package kr.co.aeye.apiserver.api.diary.dto;

import kr.co.aeye.apiserver.api.diary.dto.diaryReport.EmotionHistogram;
import kr.co.aeye.apiserver.api.diary.dto.diaryReport.MonthlyEmotion;
import kr.co.aeye.apiserver.api.diary.dto.diaryReport.SentimentLevel;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Getter
@Builder
@Data
public class GetMonthlyReportRes {
    private SentimentLevel sentimentLevel;
    private EmotionHistogram emotionHistogram;
    private MonthlyEmotion monthlyEmotion;
}
