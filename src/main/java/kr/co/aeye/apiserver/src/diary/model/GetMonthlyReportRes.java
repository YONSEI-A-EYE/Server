package kr.co.aeye.apiserver.src.diary.model;

import kr.co.aeye.apiserver.src.diary.model.diaryReport.EmotionHistogram;
import kr.co.aeye.apiserver.src.diary.model.diaryReport.MonthlyEmotion;
import kr.co.aeye.apiserver.src.diary.model.diaryReport.SentimentLevel;
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
