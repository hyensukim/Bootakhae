package com.bootakhae.userservice.global.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class AesCryptoUtil {

    private final Environment env;

    /**
     * 암호화 알고리즘
     */
    public String encrypt(String plainText) {
        try{
            String spec = Objects.requireNonNull(env.getProperty("aes.spec"));
            String keyString = Objects.requireNonNull(env.getProperty("aes.key"));
            Key key = new SecretKeySpec(keyString.getBytes(StandardCharsets.UTF_8), spec);
            Cipher cipher = Cipher.getInstance(spec);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] encrypted = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encrypted);
        }
        catch(Exception e){
            throw new RuntimeException("암호화 중 오류 : ",e);
        }
    }

    /**
     * 복호화 알고리즘
     */
    public String decrypt( String cipherText) {
        try{
            String spec = Objects.requireNonNull(env.getProperty("aes.spec"));
            String keyString = Objects.requireNonNull(env.getProperty("aes.key"));
            Key key = new SecretKeySpec(keyString.getBytes(StandardCharsets.UTF_8), spec);
            Cipher cipher = Cipher.getInstance(spec);
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(cipherText));
            return new String(decrypted, StandardCharsets.UTF_8);
        }
        catch(Exception e){
            throw new RuntimeException("복호화 중 오류 : ",e);
        }
    }
}