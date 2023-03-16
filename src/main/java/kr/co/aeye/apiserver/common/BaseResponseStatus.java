package kr.co.aeye.apiserver.common;

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
    WRONG_EMOTION(false, 400, "wrong emotion"),
    ALREADY_EXIST_EMAIL(false, 400, "already exist email"),
    //401
    WRONG_PASSWORD(false, 401, "wrong password"),
    WRONG_EMAIL(false, 401, "wrong email");



    private final boolean success;
    private final int code;
    private final String message;


    private BaseResponseStatus(boolean success, int code, String message) {
        this.success = success;
        this.code = code;
        this.message = message;
    }
}
