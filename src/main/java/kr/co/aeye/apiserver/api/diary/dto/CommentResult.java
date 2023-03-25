package kr.co.aeye.apiserver.api.diary.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Getter
@Builder
@Data
public class CommentResult {
    private int order;
    private String role;
    private String commentContent;
    private Long commentCreatedAt;
}
