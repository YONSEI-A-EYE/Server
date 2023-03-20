package kr.co.aeye.apiserver.api.user.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;


@Slf4j
public class AuthCodeGenerator {

    public static String getAuthCode(){
        String generatedString = RandomStringUtils.randomAlphanumeric(10);

        log.info("new auth code={}", generatedString);
        return generatedString;
    }
}
