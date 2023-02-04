package kr.co.aeye.apiserver.config;

import lombok.Getter;

@Getter
public enum BaseResponseStatus {

    //    200
    SUCCESS(true, 200,"success"),
    CREATED(true, 201, "create success"),

    // 400
    BAD_REQUEST(false, 400, "bad_request");

    private final boolean success;
    private final int code;
    private final String message;


    private BaseResponseStatus(boolean success, int code, String message) {
        this.success = success;
        this.code = code;
        this.message = message;
    }
}
