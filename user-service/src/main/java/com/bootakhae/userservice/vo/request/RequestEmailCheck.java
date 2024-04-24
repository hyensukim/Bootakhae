package com.bootakhae.userservice.vo.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RequestEmailCheck {
    @NotBlank(message="이메일을 입력 바랍니다.")
    private String email; // 전송 이메일
    @NotBlank(message="인증 코드를 입력 바랍니다.")
    private String code; // 검증 코드
}
