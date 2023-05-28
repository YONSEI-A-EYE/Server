package kr.co.aeye.apiserver.api.child.dto.bard;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SolutionObjectDto {
    private int index;
    private String title;
    private String content;
}
