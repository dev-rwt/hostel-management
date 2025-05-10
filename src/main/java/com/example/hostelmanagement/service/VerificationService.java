package com.example.hostelmanagement.service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.example.hostelmanagement.dto.VerificationToken;
import com.example.hostelmanagement.repository.VerificationTokenRepository;

@Service
public class VerificationService {
    @Autowired private VerificationTokenRepository tokenRepo;
    @Autowired private JavaMailSender mailSender;

    public void sendOtp(String name, String email, String password) {
        String otp = String.valueOf(100000 + new Random().nextInt(900000));
        VerificationToken vt = new VerificationToken();
        vt.setEmail(email);
        vt.setName(name);
        vt.setPassword(password);
        vt.setOtp(otp);
        vt.setExpiry(LocalDateTime.now().plusMinutes(10));
        tokenRepo.save(vt);

        String subject = "Your OTP for Registration";
        String body = "Your OTP is: " + otp + "\nIt will expire in 10 minutes.";
        sendEmail(email, subject, body);
    }

    public Optional<VerificationToken> verifyOtp(String email, String otp) {
        Optional<VerificationToken> vtOpt = tokenRepo.findByEmailAndOtp(email, otp);
        if (vtOpt.isEmpty()) return Optional.empty();
        VerificationToken vt = vtOpt.get();
        if (vt.getExpiry().isBefore(LocalDateTime.now())) return Optional.empty();

        tokenRepo.delete(vt);
        return Optional.of(vt);
    }

    private void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }
}
