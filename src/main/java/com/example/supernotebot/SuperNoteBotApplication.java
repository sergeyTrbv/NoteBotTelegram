package com.example.supernotebot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
//Аннотация, чтобы использовать функционал по таймеру
@EnableScheduling
public class SuperNoteBotApplication {

    public static void main(String[] args) {
        SpringApplication.run(SuperNoteBotApplication.class, args);


    }

}
