package kr.co.aeye.apiserver.api.diary.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PostCommentReq {
    private String commentContent;
}
