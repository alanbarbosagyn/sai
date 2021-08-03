package br.com.abce.sai.service;

import br.com.abce.sai.dto.MailDto;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {

    private final JavaMailSender mailService;

    private Environment environment;

    public MailService(JavaMailSender mailService, Environment environment) {
        this.mailService = mailService;
        this.environment = environment;
    }

    public void send(final String destinatario, final String assunto, final String corpoTexto){

        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();

        simpleMailMessage.setTo(environment.getProperty("spring.mail.username"));
        simpleMailMessage.setTo(destinatario);
        simpleMailMessage.setSubject(assunto);
        simpleMailMessage.setText(corpoTexto);

        mailService.send(simpleMailMessage);
    }

    public void send(final MailDto mailDto) {
        send(mailDto.getDestinatario(), mailDto.getAssunto(), mailDto.getCorpoTexto());
    }
}
