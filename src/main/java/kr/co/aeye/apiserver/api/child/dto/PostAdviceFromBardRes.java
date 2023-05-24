package kr.co.aeye.apiserver.api.child.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.util.ArrayList;

@Builder
@Getter
@Data
public class PostAdviceFromBardRes {
    private ArrayList<AdviceSet> advice;
}
