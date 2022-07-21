package com.example.ssodemo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.core.env.Environment;

import java.util.Arrays;

@Slf4j
@SpringBootApplication
public class SSOApplication {

    public static void main(String[] args) {
        Environment env = SpringApplication.run(SSOApplication.class, args).getEnvironment();
        log.info("started: {},CPU core: {}", Arrays.toString(env.getActiveProfiles()), Runtime.getRuntime().availableProcessors());
    }

}
