package kz.iitu.springlab.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api")
public class HelloController {

    @Value("${app.owner:unknown}")
    private String owner;

    @GetMapping("/hello")
    public Greeting hello(@RequestParam(defaultValue = "world") String name) {
        return new Greeting("Hello, " + name + "!", owner, LocalDateTime.now());
    }

    @GetMapping("/info")
    public Info info() {
        return new Info(owner,
                System.getProperty("java.version"),
                Runtime.getRuntime().availableProcessors());
    }

    @GetMapping("/bmi")
    public BmiResult getBmi(@RequestParam(required = false) Double weight,
                            @RequestParam(required = false) Double height) {
        // Обработка отсутствующего параметра (возвращаем статус 400 Bad Request)
        if (weight == null || height == null || height <= 0) {
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.BAD_REQUEST,
                    "Please provide valid weight (in kg) and height (in meters)"
            );
        }

        double bmi = weight / (height * height);
        String category;

        if (bmi < 18.5) {
            category = "Underweight";
        } else if (bmi < 24.9) {
            category = "Normal weight";
        } else if (bmi < 29.9) {
            category = "Overweight";
        } else {
            category = "Obesity";
        }

        // Округляем до двух знаков после запятой для красоты
        return new BmiResult(Math.round(bmi * 100.0) / 100.0, category);
    }

    public record BmiResult(double bmi, String category) { }

    public record Greeting(String message, String owner, LocalDateTime timestamp) { }

    public record Info(String owner, String javaVersion, int cpuCores) { }
}