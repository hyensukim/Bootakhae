package com.bootakhae.userservice.vo.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RequestLogin {
    @NotBlank(message="이메일을 입력 해주세요.")
    private String email;
    @NotBlank(message="비밀번호를 입력 해주세요.")
    private String password;
}
