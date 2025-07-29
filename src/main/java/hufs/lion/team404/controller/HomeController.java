package hufs.lion.team404.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
    
    @GetMapping("/")
    public String home() {
        return "404 Project is running!";
    }
    
    @GetMapping("/health")
    public String health() {
        return "OK";
    }
}
