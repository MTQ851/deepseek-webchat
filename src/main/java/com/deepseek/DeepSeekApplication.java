package com.deepseek;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration;
import org.springframework.context.annotation.Bean;

import java.awt.*;
import java.net.URI;
import java.net.UnknownHostException;

@SpringBootApplication(exclude = {RedisRepositoriesAutoConfiguration.class})
public class DeepSeekApplication implements CommandLineRunner {
    public static void main(String[] args) {
        SpringApplication.run(DeepSeekApplication.class, args);
    }



    @Override
    public void run(String... args) throws Exception {
        // 自动打开浏览器
        if (Desktop.isDesktopSupported()) {
            Desktop.getDesktop().browse(new URI("http://localhost:8080/"));
        } else {
            System.out.println("请手动打开浏览器访问 http://localhost:8080/");
        }
    }
}

