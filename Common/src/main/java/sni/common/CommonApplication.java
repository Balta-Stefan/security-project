package sni.common;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class CommonApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(CommonApplication.class, args);
    }

    @Bean
    public ModelMapper modelMapper(){return new ModelMapper();}

    /*
    * To do:
    * - define ModelMapper mappings for:
    *   - mapping FileEntity to DirectoryDTO and FileDTO
    *
    * */
}
