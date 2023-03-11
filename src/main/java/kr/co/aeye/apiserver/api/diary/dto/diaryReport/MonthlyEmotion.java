package kr.co.aeye.apiserver.api.diary.dto.diaryReport;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class MonthlyEmotion {
    private String emotion;
    private String comment;
}
