package com.elearning.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    public void sendSimpleEmail(String to, String subject, String text) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
            message.setFrom("noreply@elearning.com");
            javaMailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send email: " + e.getMessage());
        }
    }

    public void sendHtmlEmail(String to, String subject, String htmlContent) throws MessagingException {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);
            helper.setFrom("noreply@elearning.com");
            javaMailSender.send(message);
        } catch (MessagingException e) {
            throw new MessagingException("Failed to send HTML email", e);
        }
    }

    public void sendWelcomeEmail(String email, String firstName) {
        String subject = "Welcome to E-Learning Platform!";
        String text = "Hello " + firstName + ",\n\nWelcome to our E-Learning platform!\n" +
                "Start learning today and expand your knowledge.\n\n" +
                "Best regards,\nE-Learning Team";
        sendSimpleEmail(email, subject, text);
    }

    public void sendCourseEnrollmentEmail(String email, String firstName, String courseName) {
        String subject = "Course Enrollment Confirmation";
        String text = "Hello " + firstName + ",\n\nYou have successfully enrolled in the course: " + courseName + "\n" +
                "Start learning now!\n\n" +
                "Best regards,\nE-Learning Team";
        sendSimpleEmail(email, subject, text);
    }

    public void sendCertificateEmail(String email, String firstName, String courseName) {
        String subject = "Certificate of Completion";
        String text = "Hello " + firstName + ",\n\nCongratulations! You have successfully completed the course: " + courseName + "\n" +
                "Your certificate is now available in your dashboard.\n\n" +
                "Best regards,\nE-Learning Team";
        sendSimpleEmail(email, subject, text);
    }

    public void sendPasswordResetEmail(String email, String resetLink) {
        String subject = "Password Reset Request";
        String text = "Click the link below to reset your password:\n" + resetLink + "\n\n" +
                "If you did not request this, please ignore this email.\n\n" +
                "Best regards,\nE-Learning Team";
        sendSimpleEmail(email, subject, text);
    }
}
