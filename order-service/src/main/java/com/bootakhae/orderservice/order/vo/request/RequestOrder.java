package com.bootakhae.orderservice.order.vo.request;

import com.bootakhae.orderservice.order.dto.OrderDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@NoArgsConstructor(access =  AccessLevel.PROTECTED)
@AllArgsConstructor
public class RequestOrder {
    @NotBlank(message = "로그인 후 이용 바랍니다.")
    private String userId;
    @NotBlank(message = "우편번호를 입력 바랍니다.")
    private String address1;
    @NotBlank(message = "상세주소를 입력 바랍니다.")
    private String address2;
    @NotBlank(message = "연락처를 입력 바랍니다.")
    @Pattern(message = "휴대전화 번호 양식이 아닙니다.", regexp = "01(0|1|[6-8])\\d{3,4}\\d{4}$")
    private String phone;
    @NotNull(message="상품을 선택 바랍니다.")
    @Size(message = "상품을 최소 1개 이상 선택 바랍니다.", min = 1)
    private List<RequestOrderProduct> orderProductList;

    public OrderDto voToDto(){
        return OrderDto.builder()
                .userId(this.userId)
                .address1(this.address1)
                .address2(this.address2)
                .phone(this.phone)
                .orderProductList(this.orderProductList.stream()
                        .map(RequestOrderProduct::voToDto)
                        .collect(Collectors.toList())
                )
                .build();
    }
}
