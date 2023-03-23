package kr.co.aeye.apiserver.api.main.dto;


import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Builder
@Getter
@Data
public class GetHomeRes {
    private HomeVideo homeVideo;
    private String code;
}
