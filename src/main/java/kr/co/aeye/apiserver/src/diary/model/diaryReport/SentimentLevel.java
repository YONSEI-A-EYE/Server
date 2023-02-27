package kr.co.aeye.apiserver.src.diary.model.diaryReport;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class SentimentLevel{
    private int positive;
    private int neutral;
    private int negative;
}
