package com.chat.app;

import org.springframework.core.env.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;

@SpringBootApplication
public class ChatApplication implements CommandLineRunner {

    @Autowired
    private Environment environment;

	public static void main(String[] args) {
		SpringApplication.run(ChatApplication.class, args);
	}

    @Override
    public void run(String... args) throws Exception {
        String[] activeProfiles = environment.getActiveProfiles();
        System.out.println("=================================");
        System.out.println("Active Profile: " + Arrays.toString(activeProfiles));
        System.out.println("Database URL: " + environment.getProperty("spring.datasource.url"));
        System.out.println("=================================");
    }
}
