package br.com.abce.sai.service;

import br.com.abce.sai.exception.InfraestructureException;
import br.com.abce.sai.util.LoggerUtil;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class MailService {

    private final JavaMailSender mailSender;

    public MailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendMail(final SimpleMailMessage message) {

        try {

            mailSender.send(message);

        } catch (Exception e) {
            LoggerUtil.error(e);
            throw new InfraestructureException(e);
        }
    }
}
