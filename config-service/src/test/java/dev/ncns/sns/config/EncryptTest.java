package dev.ncns.sns.config;

import com.ulisesbocchio.jasyptspringboot.encryptor.DefaultLazyEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class EncryptTest {

    @Value("${jasypt.encryptor.password}")
    private String encryptorPassword;

    private String target;

    @Autowired
    private ConfigurableEnvironment configurableEnvironment;

    @BeforeEach
    private void beforeTest() {
        target = "";
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

}