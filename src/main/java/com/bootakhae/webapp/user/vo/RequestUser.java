package com.bootakhae.webapp.user.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RequestUser {
    @NotBlank(message = "이메일을 입력해주세요")
    @Email(message = "이메일 양식이 아닙니다.")
    private String email;

    @NotBlank(message = "비밀번호를 입력해주세요")
    @Pattern(message = "영문자, 숫자, 특수문자('!','@','#','$','%','^','&','*','(',')')를 1자 이상 입력하세요.",
            regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[!@#$%^&*()\\[\\]{}]).+")
    @Size(message = "비밀번호를 8자 이상 16자 이하로 입력하세요.",min = 8, max = 16)
    private String password;

    @NotBlank(message = "우편번호를 입력해주세요")
    private String address1; // 우편번호

    @NotBlank(message = "상세주소를 입력해주세요")
    private String address2; // 상세주소

    @NotBlank(message = "이름을 입력해주세요")
    @Pattern(message = "이름을 한글로 작성해주세요", regexp="^[가-힣]+$")
    @Size(min = 2, message = "이름은 2자 이상 입력하세요")
    private String name;

    @NotBlank(message = "닉네임을 입력해주세요")
    private String nickname;

    @NotBlank(message = "전화번호를 입력해주세요")
    @Pattern(message = "휴대전화 번호 양식이 아닙니다.", regexp = "01(0|1|[6-8])\\d{3,4}\\d{4}$")
    private String phone;

    @JsonIgnore
    private String userId;

    @JsonIgnore
    private LocalDateTime createdAt;
}
