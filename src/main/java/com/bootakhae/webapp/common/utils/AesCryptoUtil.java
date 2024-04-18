package com.bootakhae.webapp.common.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

public class AesCryptoUtil {

    /**
     * 키 반환
     */
    public static SecretKey getKey() throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES"); // 비밀키 생성객체
        keyGenerator.init(128); // 특정 크기에 대해 KeyGenerator 초기화
        return keyGenerator.generateKey(); // 비밀키 생성
    }

    /**
     * 초기화 벡터 반환
     */
    public static IvParameterSpec getIv() {
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);
        return new IvParameterSpec(iv); // 초기화 벡터 지정 및 사용자가 지정한 바이트수의 난수 바이트 생성
    }

    /**
     * 암호화 알고리즘
     */
    public static String encrypt(String specName, SecretKey key, IvParameterSpec iv,
                                 String plainText) throws Exception {
        Cipher cipher = Cipher.getInstance(specName);
        cipher.init(Cipher.ENCRYPT_MODE, key, iv);
        byte[] encrypted = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
        return new String(Base64.getEncoder().encode(encrypted));
    }

    /**
     * 복호화 알고리즘
     */
    public static String decrypt(String specName, SecretKey key, IvParameterSpec iv,
                                 String cipherText) throws Exception {
        Cipher cipher = Cipher.getInstance(specName);
        cipher.init(Cipher.DECRYPT_MODE, key, iv);
        byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(cipherText));
        return new String(decrypted, StandardCharsets.UTF_8);
    }
}