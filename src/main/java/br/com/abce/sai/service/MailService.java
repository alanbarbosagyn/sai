package br.com.abce.sai.service;

import br.com.abce.sai.dto.MailDto;
import br.com.abce.sai.exception.InfraestructureException;
import br.com.abce.sai.util.LoggerUtil;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class MailService {

    private final JavaMailSender mailService;

    @Getter @Setter
    @Value(value = "${spring.mail.username}")
    private String appRemetente;

    public MailService(JavaMailSender mailService) {
        this.mailService = mailService;
    }

    public void send(final String destinatario, final String assunto, final String corpoTexto, String mediaType){

        try {

            MimeMessage message = mailService.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(message, false, "utf-8");
            message.setContent(corpoTexto, mediaType);
            helper.setTo(destinatario);
            helper.setSubject(assunto);
            helper.setFrom(appRemetente);
            mailService.send(message);

        } catch (MessagingException ex) {
            LoggerUtil.error(ex);
            throw new InfraestructureException(ex);
        }
    }

    public void send(final MailDto mailDto) {
        send(mailDto.getDestinatario(), mailDto.getAssunto(), mailDto.getCorpoTexto(), MediaType.TEXT_HTML_VALUE);
    }
}
