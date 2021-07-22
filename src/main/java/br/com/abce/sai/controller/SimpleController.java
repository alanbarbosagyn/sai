package br.com.abce.sai.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller 
public class SimpleController {
	
    @Value("${spring.application.name}")
    String appName;

    private final JavaMailSender mailService;

    public SimpleController(JavaMailSender mailService) {
        this.mailService = mailService;
    }

    @GetMapping("/")
    public String homePage(Model model) {
        model.addAttribute("appName", appName);
        return "home";
    }

    @GetMapping("/mail")
    public String mail(SimpleMailMessage simpleMailMessage) {
        mailService.send(simpleMailMessage);
        return "E-mail enviado com sucesso!";
    }

}