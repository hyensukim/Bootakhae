package com.bootakhae.webapp.user.vo.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RequestPassword {

    @NotBlank(message = "비밀번호를 입력해주세요")
    private String password;

    @NotBlank(message = "새로운 비밀번호를 입력해주세요")
    @Pattern(message = "영문자, 숫자, 특수문자('!','@','#','$','%','^','&','*','(',')')를 1자 이상 입력하세요.",
            regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[!@#$%^&*()\\[\\]{}]).+")
    @Size(message = "비밀번호를 8자 이상 16자 이하로 입력하세요.",min = 8, max = 16)
    private String newPassword;

    @NotBlank(message = "새로운 비밀번호를 확인해주세요")
    private String confirmPassword;

}
