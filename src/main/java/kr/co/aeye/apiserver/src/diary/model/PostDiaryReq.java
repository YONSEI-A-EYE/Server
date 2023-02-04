package kr.co.aeye.apiserver.src.diary.model;

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
