package kr.co.aeye.apiserver.api.diary.dto;

import kr.co.aeye.apiserver.api.user.entity.RoleType;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Getter
@Builder
@Data
public class PostCommentRes {
    private int order;
    private Long date;
    private RoleType role;
}
