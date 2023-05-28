package kr.co.aeye.apiserver.api.child.dto.bard;

import lombok.*;

import java.util.ArrayList;

//@Builder
@NoArgsConstructor
@Getter
@Data
public class PostAdviceFromBardRes {
    private String title;
    private ArrayList<SolutionObjectDto> solutions;
}
