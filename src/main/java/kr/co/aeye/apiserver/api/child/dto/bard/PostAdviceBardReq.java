package kr.co.aeye.apiserver.api.child.dto.bard;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PostAdviceBardReq {
    private String childName;
    private int childAge;
    private String childTemperament;
    private String content;
}
