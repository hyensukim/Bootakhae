package com.bootakhae.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

@Getter
@AllArgsConstructor
public class CryptoInfoDto {
    private UserDto userDetails;
    private SecretKey key;
    private IvParameterSpec iv;
}
