package com.bootakhae.orderservice.global.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
public enum ErrorCode {

    /**
     * Common
     */
    NOT_BLANK(HttpStatus.BAD_REQUEST, "EML-ERR-01", "입력사항을 기입 바랍니다."),

    /**
     * Feign
     */
    FEIGN_CLIENT_ERROR(HttpStatus.BAD_REQUEST, "FCT-ERR-01", "잘못된 요청입니다."),
    FEIGN_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "FCT-ERR-02", "해당 서버 내부에 문제가 발생했습니다. 잠시 후 다시 이용 바랍니다."),



    /**
     * User
     */
    VALIDATION_FAIL(HttpStatus.BAD_REQUEST, "USR-ERR-01", "유효성 검증 실패했습니다."),
    NOT_JOIN_USER(HttpStatus.BAD_REQUEST, "USR-ERR_02", "가입 되지 않은 회원입니다."),
    INVALID_EMAIL(HttpStatus.BAD_REQUEST, "USR-ERR-03", "이메일 인증 실패했습니다."),
    NOT_CORRECT_PASSWORD(HttpStatus.BAD_REQUEST, "USR-ERR-04","비밀번호가 불일치합니다."),
    DUPLICATED_EMAIL(HttpStatus.BAD_REQUEST, "USR-ERR-05","중복된 이메일 입니다."),
    NOT_LOGIN_USER(HttpStatus.BAD_REQUEST,"USR-ERR-06","로그인 후 이용바랍니다."),

    /**
     * Email
     */
    FAIL_TRANSFER_EMAIL(HttpStatus.INTERNAL_SERVER_ERROR, "EML-ERR-01", "이메일 전송 중 오류 발생했습니다."),

    /**
     * Token
     */
    NOT_FOUND_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "TKN-ERR-01","토큰이 존재하지 않습니다."),
    EXPIRED_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "TKN-ERR-02", "토큰이 만료됐습니다."),
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "TKN-ERR-03", "다시 로그인 바랍니다."),

    /**
     * Product
     */
    NOT_REGISTERED_PRODUCT(HttpStatus.BAD_REQUEST, "PDT-ERR-01", "등록되지 않은 상품입니다."),

    /**
     * Wishlist
     */
    NOT_EXISTS_WISHLIST(HttpStatus.BAD_REQUEST, "WSH-ERR-01", "위시 리스트에 등록된 상품이 없습니다."),
    NOT_EXISTS_WISH(HttpStatus.BAD_REQUEST,"WSH-ERR-02","위시 리스트에 등록되어 있지 않습니다."),
    DUPLICATE_WISHLIST(HttpStatus.BAD_REQUEST, "WSH-ERR-03", "이미 등록된 상품입니다."),

    /**
     * Order
     */
    NOT_EXISTS_ORDER(HttpStatus.BAD_REQUEST, "ORD-ERR-01","주문 내역이 없습니다."),
    LACK_PRODUCT_STOCK(HttpStatus.BAD_REQUEST, "ORD-ERR-02", "상품 재고가 부족합니다."),

    NOT_DONE_STATUS(HttpStatus.BAD_REQUEST, "ORD_ERR-03", "현재 배송이 완료되지 않았습니다. 배송 완료 후 반품 요청 바랍니다."),
    NOT_RETURN_DURATION(HttpStatus.BAD_REQUEST, "ORD-ERR-04", "배송 완료 후 1일이 지나 반품이 불가합니다.");


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
