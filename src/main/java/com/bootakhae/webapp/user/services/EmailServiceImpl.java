package com.bootakhae.webapp.user.services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender emailSender;
    private final Environment env;
    private final JavaMailSender javaMailSender;
    private final HttpSession session;

    @Override
    public void sendMessage(String to){
        log.debug("이메일 인증 : 메시지 전송");
        try {
            MimeMessage mimeMessage = createMessage(to);
            javaMailSender.send(mimeMessage);
        }catch(MessagingException e){
            throw new RuntimeException("이메일 인증 : 생성 및 전송 중 오류발생");
        }
    }

    @Override
    public boolean verifyCode(String email, String code){
        String savedCode = String.valueOf(session.getAttribute(email));
        log.debug("이메일 인증 코드 확인 : {}", savedCode);
        if(Objects.isNull(savedCode)){
            return false;
        }
        return code.equals(savedCode);
    }

    private MimeMessage createMessage(String to) throws MessagingException{
        log.debug("이메일 인증 : 메시지 생성");

        String subject = "[BooTakHae] 회원가입 인증 안내";
        String from = env.getProperty("spring.mail.username")+"@gmail.com";
        String code = makeCode();

        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message,true,"UTF-8");

        mimeMessageHelper.setTo(to);
        mimeMessageHelper.setSubject(subject);
        mimeMessageHelper.setFrom(from);
        mimeMessageHelper.setText(setContext(code),true);

        session.setAttribute(to,code);

        return message;
    }

    //todo 아래 코드 이해하기
    private String makeCode(){
//        //Given
//        char[] charNums = new char[] { '0','1','2','3','4','5','6','7','8','9'};
//        char[] charAlphas = new char[] {'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R',
//                'S','T','U','V','W','X','Y','Z'};
//        char[] charSpecials = new char[] {'!','@','#','$','%','^','&'};
//        StringBuilder str = new StringBuilder();
//
//        //When
//        int idx = 0;
//        for (int i = 0; i < 3; i++) {
//            idx = (int) (charNums.length * Math.random());
//            str.append(charNums[idx]);
//        }
//
//        for(int i=0; i < 3; i++){
//            idx = (int) (charAlphas.length * Math.random());
//            str.append(charAlphas[idx]);
//        }
//
//        for(int i=0; i < 2; i++){
//            idx = (int) (charSpecials.length * Math.random());
//            str.append(charSpecials[idx]);
//        }
//
//        //Then
//        return str.toString();

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
