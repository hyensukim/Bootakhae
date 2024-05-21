package com.bootakhae.gatewayserver.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
public enum ErrorCode {

    NOT_BLANK(HttpStatus.BAD_REQUEST, "EML-ERR-01", "입력사항을 기입 바랍니다."),

    /**
     * User
     */
    VALIDATION_FAIL(HttpStatus.BAD_REQUEST, "USR-ERR-01", "유효성 검증 실패했습니다."),
    NOT_FOUND_USER(HttpStatus.BAD_REQUEST, "USR-ERR_02", "가입 되지 않은 회원입니다."),
    INVALID_EMAIL(HttpStatus.BAD_REQUEST, "USR-ERR-03", "이메일 인증 실패했습니다."),
    NOT_CORRECT_PASSWORD(HttpStatus.BAD_REQUEST, "USR-ERR-04","비밀번호가 불일치합니다."),
    DUPLICATED_EMAIL(HttpStatus.BAD_REQUEST, "USR-ERR-05","중복된 이메일 입니다."),

    /**
     * Email
     */
    FAIL_TRANSFER_EMAIL(HttpStatus.INTERNAL_SERVER_ERROR, "EML-ERR-02", "이메일 전송 중 오류 발생했습니다."),

    /**
     * Token
     */
    NOT_FOUND_REFRESH_TOKEN(HttpStatus.INTERNAL_SERVER_ERROR, "TKN-ERR-01","토큰이 존재하지 않습니다."),
    NOT_FOUND_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "TKN-ERR-02", "로그인 후 이용 바랍니다."),
    EXPIRED_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "TKN-ERR-03", "토큰이 만료됐습니다."),
    INVALID_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "TKN-ERR-04", "토큰이 유효하지 않습니다."),
    NOT_ACCESSIBLE_AUTHORITY(HttpStatus.FORBIDDEN,"TKN-ERR-05","접근 권한이 없습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime timestamp;

    ErrorCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }
}
