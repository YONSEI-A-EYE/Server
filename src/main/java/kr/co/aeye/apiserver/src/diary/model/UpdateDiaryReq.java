package kr.co.aeye.apiserver.src.diary.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateDiaryReq {
    private String emotion;
    private String content;
}
