package sni.admin_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {"sni.common", "sni.admin_service"})
@EntityScan(basePackages = "sni.common")
@EnableJpaRepositories(basePackages = "sni.common")
@PropertySource("admin-service-application.properties")
public class AdminApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(AdminApplication.class, args);
    }
}
