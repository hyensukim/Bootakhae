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

    private String function;

    private String type;

    private String producer;

    private String nutritionFacts;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long stock;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long qty;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LocalDateTime eventTime;
}
