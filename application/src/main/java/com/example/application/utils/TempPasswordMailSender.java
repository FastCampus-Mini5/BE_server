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

  private static final String MAIL_SUBJECT = "[당연하지] 임시 비밀번호 메일이 도착했습니다.";

  private final JavaMailSender mailSender;

  @Value("${spring.mail.username}")
  private String from;

  public void send(String to, String tempPassword) throws Exception {
    MimeMessage message = mailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");

    StringBuffer text = new StringBuffer();
    text.append("<!DOCTYPE html>");
    text.append("<html>");
    text.append("<body>");
    text.append(
            " <div" +
                    "	style=\"font-family: 'Apple SD Gothic Neo', 'sans-serif' !important; width: 600px; height: 600px; border-top: 4px solid #2656F6; " +
                    "       margin: 100px auto; padding: 30px 0; box-sizing: border-box;\">" +
                    "	<h1 style=\"margin: 0; padding: 0 5px; font-size: 28px; font-weight: 400;\">" +
                    "		<span style=\"color: #2656F6; font-weight: 800;\">당연하지</span> 임시 비밀번호 발급" +
                    "	</h1>\n" +
                    "	<h1 style=\"color: black; text-align: center; margin-bottom: 3rem; font-size: 40px;;\"" +
                    "		<p style=\"display: inline-block; font-weight: 1200; width: 210px; height: 45px; margin: 30px 5px 40px; color: black; line-height: 45px; vertical-align: middle; font-size: 40px;\">" +
                    "			"+ tempPassword + "</p></h1>" +
                    "   <p>위의 임시 비밀번호로 로그인 후, 안전한 비밀번호로 변경해주세요!</p>" +
                    "	<div style=\"border-top: 1px solid #DDD; padding: 5px; margin-top: 1rem;\"></div>" +
                    " </div>"
    );
    text.append("</body>");
    text.append("</html>");

    helper.setFrom(from);
    helper.setTo(to);
    helper.setSubject(MAIL_SUBJECT);
    helper.setText(text.toString(), true);

    mailSender.send(message);
  }
}
