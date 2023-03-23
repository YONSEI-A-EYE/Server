package kr.co.aeye.apiserver.api.main.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Builder
@Getter
@Data
public class HomeVideo {
    private String videoUrl;
    private String title;
}
