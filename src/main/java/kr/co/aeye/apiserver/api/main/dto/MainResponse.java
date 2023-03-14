package kr.co.aeye.apiserver.api.main.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Getter
@Builder
@Data
public class MainResponse {
    private String message;
}
