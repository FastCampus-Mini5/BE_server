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

    MimeMessage message = mailSender.createMimeMessage();

    MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");

    StringBuffer text = new StringBuffer();
    text.append("<!DOCTYPE html>\n");
    text.append("<html>\n");
    text.append("<body>\n");
    text.append(
        " <div style=\"font-family: 'Apple SD Gothic Neo', 'sans-serif' !important; width: 600px; height: 600px; border-top: 4px solid #2656F6; margin: 100px auto; padding: 30px 0; box-sizing: border-box;\">\n");
    text.append("  <h1 style=\"margin: 0; padding: 0 5px; font-size: 28px; font-weight: 400;\">\n");
    text.append("    <span style=\"color: #2656F6; font-weight: 800;\">당연하지</span>  임시 비밀번호 발급\n");
    text.append("  </h1>\n");
    text.append("  <div align=center>\n");
    text.append(
        "   <h1 style=\"color: black; align: center; margin: 2rem; font-size: 40px;\">\n");
    text.append(
        "    <p style=\"display: inline-block; font-weight: 1200; width: 210px; height: 45px; margin: 30px 5px 40px; color: black; line-height: 45px; vertical-align: middle; font-size: 40px;\">");
    text.append(tempPassword);
    text.append("</p>\n");
    text.append("   </h1>\n");
    text.append("  </div>\n");
    text.append("  <p>위의 임시 비밀번호로 로그인 후, 안전한 비밀번호로 변경해주세요!</p>\n");
    text.append("  <div style=\"border-top: 1px solid #DDD; padding: 5px; margin-top: 1rem;\"/>\n");
    text.append(" </div>\n");
    text.append("</body>\n");
    text.append("</html>");

    try {
      helper.setFrom(from);
      helper.setTo(to);
      helper.setSubject(MAIL_SUBJECT);
      helper.setText(text.toString(), true);

      mailSender.send(message);
    } catch (MessagingException msg) {
      throw new Exception500(ErrorMessage.EMAIL_MESSAGE_BUILD_FAILED);
    } catch (MailException e) {
      throw new MailSendFailureException();
    }
  }
}
