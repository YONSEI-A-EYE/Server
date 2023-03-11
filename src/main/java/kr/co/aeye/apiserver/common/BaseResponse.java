package kr.co.aeye.apiserver.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
@JsonPropertyOrder({"success", "code", "message", "result"})
public class BaseResponse<T>{
    @JsonProperty("success")
    private final Boolean success;
    private final int code;
    private final String message;
    private T result;

    // 요청에 성공한 경우
    public BaseResponse(BaseResponseStatus status, T result) {
        this.success = status.isSuccess();
        this.code = status.getCode();
        this.message = status.getMessage();
        this.result = result;
    }

    // 요청에 실패한 경우
    public BaseResponse(BaseResponseStatus status) {
        this.success = status.isSuccess();
        this.code = status.getCode();
        this.message = status.getMessage();
    }
}

