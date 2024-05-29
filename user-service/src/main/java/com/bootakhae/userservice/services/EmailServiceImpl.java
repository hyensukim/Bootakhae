package com.bootakhae.userservice.services;

import com.bootakhae.userservice.global.exception.CustomException;
import com.bootakhae.userservice.global.exception.ErrorCode;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final Environment env;
    private final JavaMailSender emailSender;
    private final JavaMailSender javaMailSender;
    private final StringRedisTemplate stringRedisTemplate;

    private static final String OTP_PREFIX = "otp:";

    @Override
    public void sendMessage(String to){
        log.debug("이메일 인증 : 메시지 전송");

        if(to.isBlank()) throw new CustomException(ErrorCode.NOT_BLANK);

        try {
            MimeMessage mimeMessage = createMessage(to);
            javaMailSender.send(mimeMessage);
        }catch(MessagingException e){
            throw new CustomException(ErrorCode.FAIL_TRANSFER_EMAIL);
        }
    }

    @Override
    public boolean verifyCode(String email, String code){
        log.debug("이메일 인증 : 인증 코드 검증");
        if(email.isBlank() || code.isBlank()) throw new CustomException(ErrorCode.NOT_BLANK);

        String key = OTP_PREFIX + email;

        if(Boolean.TRUE.equals(stringRedisTemplate.hasKey(key))){
            String savedCode = stringRedisTemplate.opsForValue().get(OTP_PREFIX + email);
            log.debug("이메일 인증 코드 확인 : {}", savedCode);
            return code.equals(savedCode);
        }
        else{
            return false;
        }
    }

    private MimeMessage createMessage(String to) throws MessagingException{
        log.debug("이메일 인증 : 메시지 생성");

        String subject = "[BooTakHae] 회원가입 인증 안내";
        String from = env.getProperty("spring.mail.username")+"@gmail.com";
        long limitTime = Long.parseLong(Objects.requireNonNull(env.getProperty("redis.otp.ttl")));
        String code = makeCode();

        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message,true,"UTF-8");

        mimeMessageHelper.setTo(to);
        mimeMessageHelper.setSubject(subject);
        mimeMessageHelper.setFrom(from);
        mimeMessageHelper.setText(setContext(code),true);

        stringRedisTemplate.opsForValue().set(OTP_PREFIX + to, code, limitTime, TimeUnit.SECONDS);

        return message;
    }

    //todo 아래 코드 이해하기
    private String makeCode(){
        int leftLimit = 48; // number '0'
        int rightLimit = 122; // alphabet 'z'
        int targetStringLength = 6;
        Random random = new Random();

        return random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 | i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    private String setContext(String code){
        return "<p> 안녕하세요. \n"
                + "<p> 귀하께서 요청하신 이메일 인증을 위해 </p> \n"
                + "<br>"
                + "<p> 발송된 메일 입니다.</p> \n"
                + "<p> 인증코드 : <h2>\"" + code +"\"</h2></p>"
                + "<br>";
    }
}
