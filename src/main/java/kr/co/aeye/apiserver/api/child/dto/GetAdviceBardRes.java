package kr.co.aeye.apiserver.api.child.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Getter
@Builder
@Data
public class GetAdviceBardRes {
    private String childName;
    private int childAge;
    private String childTemperament;
}
