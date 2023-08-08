package com.example.application.user.service;

import com.example.core.errors.ErrorMessage;
import com.example.core.errors.exception.Exception500;
import com.example.core.errors.exception.MailSendFailureException;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {
    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;

    public void send(String to, String tempPassword) {
        final String MAIL_SUBJECT = "당연하지 임시 비밀번호 메일이 도착했습니다.";
        final String MAIL_CONTEXT_PREFIX = "임시 비밀번호: ";

        MimeMessage message = mailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");

        try {
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(MAIL_SUBJECT);
            helper.setText(MAIL_CONTEXT_PREFIX + tempPassword);
            mailSender.send(message);
        } catch (MessagingException msg) {
            throw new Exception500(ErrorMessage.EMAIL_MESSAGE_BUILD_FAILED);
        } catch (MailException e) {
            throw new MailSendFailureException();
        }
    }
}
