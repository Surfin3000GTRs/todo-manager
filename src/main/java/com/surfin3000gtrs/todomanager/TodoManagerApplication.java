package com.surfin3000gtrs.todomanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TodoManagerApplication {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(TodoManagerApplication.class);
        if (args.length > 0 && "cli".equalsIgnoreCase(args[0])) {
            application.setWebApplicationType(WebApplicationType.NONE);
        }
        application.run(args);
    }
}
