package kr.co.aeye.apiserver.auth.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Getter
@Builder
@Data
public class PatchCodeRes {
    private String code;
}
