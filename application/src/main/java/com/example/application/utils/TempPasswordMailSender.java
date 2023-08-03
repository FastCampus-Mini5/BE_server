package com.example.application.utils;

import javax.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TempPasswordMailSender {

  private static final String MAIL_SUBJECT = "당연하지 임시 비밀번호 메일이 도착했습니다.";
  private static final String MAIL_CONTEXT_PREFIX = "임시 비밀번호: ";

  private final JavaMailSender mailSender;

  @Value("${spring.mail.username}")
  private String from;

  public void send(String to, String tempPassword) throws Exception {
    MimeMessage message = mailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");

    helper.setFrom(from);
    helper.setTo(to);
    helper.setSubject(MAIL_SUBJECT);
    helper.setText(MAIL_CONTEXT_PREFIX + tempPassword);

    mailSender.send(message);
  }
}
