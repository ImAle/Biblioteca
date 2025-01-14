package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import com.example.demo.service.EmailService;

@RestController
@RequestMapping("/email")
public class EmailController {

    @Autowired
    @Qualifier("emailService")
    private EmailService emailService;

    @PostMapping("/send")
    public String sendEmail(@RequestParam String to, 
                            @RequestParam String subject, 
                            @RequestParam String text) {
        emailService.sendSimpleEmail(to, subject, text);
        return "Email enviado a " + to;
    }
}

