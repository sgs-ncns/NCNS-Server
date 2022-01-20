package dev.ncns.sns.auth.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class VariousGenerator {

    public static String port;

    public VariousGenerator(@Value("${server.port}") String port) {
        VariousGenerator.port = port;
    }

    public static String getRefreshTokenKey(String userId) {
        return JwtProvider.REFRESH_TOKEN_NAME + "[" + userId + "]";
    }

    public static String getBlackListTokenKey(String userId) {
        return "BlackListToken" + "[" + userId + "]";
    }

    public static int getResponseCode(String code) {
        String result = port.substring(1) + code;
        return Integer.parseInt(result);
    }

}
