package kr.co.aeye.apiserver.api.user.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RoleType {
    USER("ROLE_USER", "일반 사용자 권한"),
    ADMIN("ROLE_ADMIN", "관리자 권한"),
    MAIN_PARENT("ROLE_MAIN_PARENT", "주 양육자"),
    SUB_PARENT("ROLE_SUB_PARENT", "부 양육자");

    private final String code;
    private final String title;
}
