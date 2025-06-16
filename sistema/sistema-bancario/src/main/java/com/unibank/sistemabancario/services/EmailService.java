package com.unibank.sistemabancario.services;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void enviarEmail(String para, String assunto, String texto) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(para);
            message.setSubject(assunto);
            message.setText(texto);
            mailSender.send(message);
            System.out.println("Enviado com sucesso!");
        } catch (Exception e) {
            System.err.println("Erro ao enviar email: " + e.getMessage());
            e.printStackTrace();
        }
    }
}