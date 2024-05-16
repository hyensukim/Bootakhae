package com.bootakhae.webapp.wishlist.dto.request;

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
    private String productId;

    private String userId;

    @NotNull(message = "수량을 입력 바랍니다.")
    private Long quantity;
}
