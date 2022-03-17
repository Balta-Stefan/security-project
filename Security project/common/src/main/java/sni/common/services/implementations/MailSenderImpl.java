package sni.common.services.implementations;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import sni.common.services.MailSender;

@Service
@Slf4j
public class MailSenderImpl implements MailSender
{
    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String sender;

    public MailSenderImpl(JavaMailSender mailSender)
    {
        this.mailSender = mailSender;
    }


    @Override
    @Async
    public void sendMail(String receiver, String title, String message)
    {
        try
        {
            SimpleMailMessage mail = new SimpleMailMessage();
            mail.setFrom(this.sender);
            mail.setTo(receiver);
            mail.setSubject(title);
            mail.setText(message);

            this.mailSender.send(mail);
        }
        catch(Exception e)
        {
            log.warn("Mail notification unsuccessful", e);
        }
    }
}
