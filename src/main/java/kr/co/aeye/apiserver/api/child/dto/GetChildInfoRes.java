package kr.co.aeye.apiserver.api.child.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Getter
@Builder
@Data
public class GetChildInfoRes {
    private Long id;
    private String name;
    private int age;
    private String temperament;
}
