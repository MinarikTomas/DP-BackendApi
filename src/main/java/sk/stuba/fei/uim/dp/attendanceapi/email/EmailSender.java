package sk.stuba.fei.uim.dp.attendanceapi.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class EmailSender {
    @Autowired
    private JavaMailSender mailSender;


    public void sendEmail(String email, String subject, String content){
        System.out.println("sending email");
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("tminarik20@gmail.com");
        message.setTo(email);
        message.setSubject(subject);
        message.setText(content);
        mailSender.send(message);
    }
}
