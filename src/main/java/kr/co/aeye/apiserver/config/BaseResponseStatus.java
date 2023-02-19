package kr.co.aeye.apiserver.config;

import lombok.Getter;

@Getter
public enum BaseResponseStatus {

    //    200
    SUCCESS(true, 200,"success"),
    CREATED(true, 201, "create success"),

    // 400
    BAD_REQUEST(false, 400, "bad request"),
    USER_NOT_FOUND(false, 400, "user not found"),
    DIARY_NOT_FOUND(false, 400, "diary not found"),
    DIARY_ALREADY_EXIST(false, 400, "diary duplicate error"),
    WRONG_EMOTION(false, 400, "wrong emotion");


    private final boolean success;
    private final int code;
    private final String message;


    private BaseResponseStatus(boolean success, int code, String message) {
        this.success = success;
        this.code = code;
        this.message = message;
    }
}
