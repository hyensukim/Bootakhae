package com.bootakhae.payservice.global.jasypt;

import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JasyptConfig {

    @Value("${jasypt.encryptor.password}")
    private String PASSWORD;

    private String ALGORITHM = "PBEWithMD5AndDES";

    @Bean
    public StringEncryptor jasyptStringEncryptor(){
        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        SimpleStringPBEConfig config = new SimpleStringPBEConfig();

        config.setPassword(PASSWORD);
        config.setPoolSize(2);
        config.setAlgorithm(ALGORITHM);
        config.setStringOutputType("base64");
        config.setKeyObtentionIterations("1000"); // 반복할 해싱 횟수
        config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
        encryptor.setConfig(config);

        return encryptor;
    }
}