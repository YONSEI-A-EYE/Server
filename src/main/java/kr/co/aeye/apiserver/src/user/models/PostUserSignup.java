package kr.co.aeye.apiserver.src.user.models;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PostUserSignup {
    private String email;
    private String name;
    private String password;
}
