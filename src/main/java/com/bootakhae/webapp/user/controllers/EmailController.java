package com.bootakhae.webapp.user.controllers;

import com.bootakhae.webapp.user.services.EmailService;
import com.bootakhae.webapp.user.vo.request.RequestEmailCheck;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/users/email")
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;

    /**
     * 인증 코드 메일 전송
     */
    @PostMapping("send")
    public ResponseEntity<String> sendEmail(@RequestBody RequestEmailCheck request) {
        emailService.sendMessage(request.getEmail());
        return ResponseEntity.status(HttpStatus.OK).body("인증 코드가 발송되었습니다.");
    }

    /**
     * 인증 코드 확인
     */
    @PostMapping("verify")
    public ResponseEntity<String> verifyCode(@Valid @RequestBody RequestEmailCheck request) {
        boolean isChecked = emailService.verifyCode(request.getEmail(), request.getCode());

        if(isChecked) {
            return ResponseEntity.status(HttpStatus.OK).body("인증이 완료되었습니다.");
        }
        else{
            return ResponseEntity.status(HttpStatus.OK).body("인증에 실패했습니다.");
        }
    }
}
