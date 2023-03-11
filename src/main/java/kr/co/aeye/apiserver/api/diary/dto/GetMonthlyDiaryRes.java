package kr.co.aeye.apiserver.api.diary.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Getter
@Builder
@Data
public class GetMonthlyDiaryRes {
    private Long id;
    private String emotion;
    private String content;
}
