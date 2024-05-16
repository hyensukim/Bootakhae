package com.bootakhae.productservice.vo.request;

import com.bootakhae.productservice.dto.ProductDto;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RequestProduct {

    @NotBlank(message="상품명을 입력해주세요")
    @Size(message="최대 25자까지 입력 가능합니다.",max=25)
    private String name;

    @NotNull(message="상품 가격을 입력해주세요")
    @Min(message="최소 0원 이상부터 입력해주세요", value = 0)
    private Long price;

    @NotNull(message="재고를 입력해주세요")
    @Min(message="최소 0개 이상부터 입력해주세요", value = 0)
    private Long stock;

    @NotBlank(message="제조사를 입력해주세요")
    @Size(message="최대 25자까지 입력 가능합니다.", max=25)
    private String producer;

    @NotBlank(message="상품 기능을 선택해주세요")
    @Size(message="최대 25자까지 입력 가능합니다.", max=25)
    private String function;

    @NotBlank(message="상품 형태를 선택해주세요")
    @Size(message="최대 25자까지 입력 가능합니다.", max=25)
    private String type;

    @NotBlank(message="성분표를 입력해주세요")
    @Size(message="최대 250자까지 입력 가능합니다.", max = 250)
    private String nutritionFacts;

    public ProductDto voToDto(){
        return ProductDto.builder()
                .name(this.name)
                .price(this.price)
                .stock(this.stock)
                .producer(this.producer)
                .function(this.function)
                .type(this.type)
                .nutritionFacts(this.nutritionFacts)
                .build();
    }
}
