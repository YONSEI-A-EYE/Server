package kr.co.aeye.apiserver.auth.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PatchCodeReq {
    private String role;
    private String code;
}
