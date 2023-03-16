package kr.co.aeye.apiserver.auth.dto;

import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PostLoginReq {
    private String email;
    private String password;
}
