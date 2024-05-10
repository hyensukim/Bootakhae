package com.bootakhae.productservice.vo.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponseProduct {
    private String productId;

    private String name;

    private Long price;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long stock;

    private String producer;

    private String nutritionFacts;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LocalDateTime eventTime;
}
