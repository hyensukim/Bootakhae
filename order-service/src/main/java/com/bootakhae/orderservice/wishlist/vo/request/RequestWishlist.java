package com.bootakhae.orderservice.wishlist.vo.request;

import com.bootakhae.orderservice.order.dto.OrderDto;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestWishlist {
    @NotBlank(message = "로그인 후 이용 바랍니다.")
    private String userId;
    @NotBlank(message = "연락처를 입력 바랍니다.")
    private String phone;
    /**
     *  페이지 상에서 기본 주소 vs. 직접 입력 선택하도록 함
     *  기본 주소 선택 -> address1 : basic & address2 : basic 이라고 나타난다고 가정.
     *  직접 입력 선택 -> address1 : 입력값 & address2 : 입력값
     */
//    @NotNull(message = "배송지 옵션을 선택 바랍니다.")
//    private Boolean chooseBasicAddress;
    @NotBlank(message = "우편번호를 입력 바랍니다.")
    private String address1;
    @NotBlank(message = "상세주소를 입력 바랍니다.")
    private String address2;
    @NotBlank(message = "결제수단을 입력 바랍니다.")
    private String payMethod;

    public OrderDto voToDto(){
        return OrderDto.builder()
                .userId(this.userId)
                .address1(this.address1)
                .address2(this.address2)
                .phone(this.phone)
                .build();
    }
}
