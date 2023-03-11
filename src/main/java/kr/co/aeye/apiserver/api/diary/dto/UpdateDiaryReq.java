package kr.co.aeye.apiserver.api.diary.dto;

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
