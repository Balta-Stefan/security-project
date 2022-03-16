package sni.admin_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication(scanBasePackages = {"sni.common", "sni.admin_service"})
@PropertySource("admin-service-application.properties")
public class AdminApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(AdminApplication.class, args);
    }
}
