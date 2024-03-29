package kr.co.aeye.apiserver.api.diary.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Getter
@Builder
@Data
public class PostCommentRes {
    private int order;
    private Long date;
    private String role;
}
