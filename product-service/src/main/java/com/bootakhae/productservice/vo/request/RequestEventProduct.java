package com.bootakhae.productservice.vo.request;

import com.bootakhae.productservice.dto.ProductDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RequestEventProduct {
    @NotBlank(message="상품을 선택 바랍니다.")
    private String productId;
    @NotNull(message="이벤트 날짜를 선택바랍니다.")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime eventTime;

    public ProductDto voToDto() {
        return ProductDto.builder()
                .productId(this.productId)
                .eventTime(this.eventTime)
                .build();
    }
}
