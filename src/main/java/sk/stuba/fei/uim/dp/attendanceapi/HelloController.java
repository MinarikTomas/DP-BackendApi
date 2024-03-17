package sk.stuba.fei.uim.dp.attendanceapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import sk.stuba.fei.uim.dp.attendanceapi.email.EmailSender;

@RestController
public class HelloController {

    @Autowired
    EmailSender emailSender;
    @GetMapping("/api/")
    public String index() {
//        emailSender.sendEmail("tminarik20@gmail.com", "test mail", "Hello");
        return "Greetings from Spring Boot!";
    }
}
