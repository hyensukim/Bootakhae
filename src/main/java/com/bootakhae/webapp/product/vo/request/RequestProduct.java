package com.bootakhae.webapp.product.vo.request;

import com.bootakhae.webapp.product.dto.ProductDto;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RequestProduct {

    @NotBlank(message="상품명을 입력해주세요")
    @Max(message="최대 25자까지 입력 가능합니다.",value=25)
    private String name;

    @NotNull(message="상품 가격을 입력해주세요")
    @Min(message="최소 0원 이상부터 입력해주세요", value = 0)
    private Integer price;

    @NotNull(message="재고를 입력해주세요")
    @Min(message="최소 0개 이상부터 입력해주세요", value = 0)
    private Integer stock;

    @NotBlank(message="제품 업체를 입력해주세요")
    @Max(message="최대 25자까지 입력 가능합니다.",value=25)
    private String producer;

    @NotBlank(message="성분표를 입력해주세요")
    @Max(message="최대 250작까지 입력 가능합니다.", value=250)
    private String nutritionFacts;

    public ProductDto voToDto(){
        return ProductDto.builder()
                .name(this.name)
                .price(this.price)
                .stock(this.stock)
                .producer(this.producer)
                .nutritionFacts(this.nutritionFacts)
                .build();
    }
}
