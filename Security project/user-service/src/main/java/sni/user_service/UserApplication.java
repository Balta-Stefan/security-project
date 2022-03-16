package sni.user_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication(scanBasePackages = {"sni.common", "sni.user_service"})
@PropertySource("user-service-application.properties")
public class UserApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(UserApplication.class, args);
    }
}
