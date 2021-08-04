package br.com.abce.sai.controller;

import br.com.abce.sai.dto.MailDto;
import br.com.abce.sai.service.MailService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/notificacao")
@Api
public class MailController {

    private MailService mailService;

    public MailController(MailService mailService) {
        this.mailService = mailService;
    }

    @PostMapping("/mail")
    public void mail(@Valid @RequestBody MailDto mailDto) {

        mailService.send(mailDto);
    }
}
