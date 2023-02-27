package kr.co.aeye.apiserver.src.diary.utils;

import kr.co.aeye.apiserver.src.diary.model.Diary;
import kr.co.aeye.apiserver.src.diary.model.diaryReport.SentimentLevel;

import java.util.List;

public class GetMonthlySentimentLevel {
    public static SentimentLevel getMonthlySentimentLevel(List<Diary>diaryList) {
        int negative = 0;
        int neutral = 0;
        int positive = 0;
        for (Diary diary : diaryList) {
            float sentiment = GetSentimentLevel.getSentimentLevel(diary.getScore(), diary.getMagnitude());
            if (sentiment <= 1.66) {
                negative += 1;
            } else if (sentiment <= 3.33) {
                neutral += 1;
            } else {
                positive += 1;
            }
        }
        return SentimentLevel
                .builder()
                .positive(positive)
                .negative(negative)
                .neutral(neutral)
                .build();

    }
}
