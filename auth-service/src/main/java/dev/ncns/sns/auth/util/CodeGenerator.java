package dev.ncns.sns.auth.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CodeGenerator {

    public static String port;

    public CodeGenerator(@Value("${server.port}") String port) {
        CodeGenerator.port = port;
    }

    public static String getResponseCode(String code) {
        return port.substring(2) + code;
    }

}
