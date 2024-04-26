package com.bootakhae.orderservice.wishlist.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestWishDto {
    @NotBlank(message = "상품을 선택해주세요.")
    private String productId;

    @NotBlank(message = "로그인 후 이용바랍니다.")
    private String userId;

    @NotNull(message = "수량을 입력 바랍니다.")
    private Long qty;
}
