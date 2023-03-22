package kr.co.aeye.apiserver.api.child.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PostChildReq {
    private String name;
    private String temperament;
}
