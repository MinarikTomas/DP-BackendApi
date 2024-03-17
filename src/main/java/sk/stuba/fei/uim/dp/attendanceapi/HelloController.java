package sk.stuba.fei.uim.dp.attendanceapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import sk.stuba.fei.uim.dp.attendanceapi.email.EmailSender;

@RestController
public class HelloController {
    @GetMapping("/api/")
    public String index() {
        return "Greetings from Spring Boot!";
    }
}
