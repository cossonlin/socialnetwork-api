package com.cosson;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ApiServerApplication implements CommandLineRunner {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public static void main(String[] args) {
        SpringApplication.run(ApiServerApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        logger.info("");
    }
}
