package kr.co.aeye.apiserver.api.user.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum RoleType {
    USER("ROLE_USER", "일반 사용자 권한"),
    ADMIN("ROLE_ADMIN", "관리자 권한"),
    MAIN_PARENT("MAIN_PARENT", "주 양육자"),
    SUB_PARENT("SUB_PARENT", "부 양육자");



    private final String code;
    private final String displayName;

    public static RoleType of(String code) {
        return Arrays.stream(RoleType.values())
                .filter(r -> r.getCode().equals(code))
                .findAny()
                .orElse(USER);
    }
}
