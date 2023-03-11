package kr.co.aeye.apiserver.api.diary.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PostDiaryReq {
    private int year;
    private int month;
    private int day;
    private String content;
}
