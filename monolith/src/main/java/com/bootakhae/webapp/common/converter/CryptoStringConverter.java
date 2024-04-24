package com.bootakhae.webapp.common.converter;

import com.bootakhae.webapp.common.utils.AesCryptoUtil;
import jakarta.persistence.AttributeConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CryptoStringConverter implements AttributeConverter<String, String> {

    private final AesCryptoUtil aesCryptoUtil;

    @Override
    public String convertToDatabaseColumn(String attribute) {
        if(attribute == null || attribute.isEmpty()) return null;
        return aesCryptoUtil.encrypt(attribute);
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        if(dbData == null || dbData.isEmpty()) return null;
        return aesCryptoUtil.decrypt(dbData);
    }
}
