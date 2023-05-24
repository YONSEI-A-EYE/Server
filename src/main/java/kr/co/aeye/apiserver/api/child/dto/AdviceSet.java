package kr.co.aeye.apiserver.api.child.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Builder
@Getter
@Data
public class AdviceSet {
    private String title;
    private String content;
}
