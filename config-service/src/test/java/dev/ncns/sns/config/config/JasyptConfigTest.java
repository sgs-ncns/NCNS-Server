package dev.ncns.sns.config.config;

import com.ulisesbocchio.jasyptspringboot.encryptor.DefaultLazyEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.ConfigurableEnvironment;

@SpringBootTest
public class JasyptConfigTest {

    @Value("${jasypt.encryptor.password}")
    private String encryptorPassword;

    @Value("${jwt.secret}")
    private String secret;

    private String target;

    @Autowired
    private ConfigurableEnvironment configurableEnvironment;

    @BeforeEach
    private void beforeTest() {
        target = "target"; // Enter target to be encrypted
    }

    @Test
    public void standardPBEStringEncryptorTest() {
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setPassword(encryptorPassword);
        System.out.println("StandardPBEStringEncryptor : " + encryptor.encrypt(target));
    }

    @Test
    public void defaultLazyEncryptorTest() {
        DefaultLazyEncryptor encryptor = new DefaultLazyEncryptor(configurableEnvironment);
        System.out.println("DefaultLazyEncryptor : " + encryptor.encrypt(target));
    }

    @Test
    public void pooledPBEStringEncryptorTest() {
        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        encryptor.setPoolSize(1);
        encryptor.setPassword(encryptorPassword);
        System.out.println("PooledPBEStringEncryptor : " + encryptor.encrypt(target));
    }

    @Test
    public void decryptTest() {
        System.out.println(secret);
    }

}
