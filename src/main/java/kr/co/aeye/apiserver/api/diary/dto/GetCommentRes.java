package kr.co.aeye.apiserver.api.diary.dto;


import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@Data
public class GetCommentRes {
    private String content;
    private String emotion;
    private List<CommentResult> comments;
}
