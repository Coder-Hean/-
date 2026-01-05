package com.shop.bank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.awt.Desktop;
import java.net.URI;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class IronBankSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(IronBankSystemApplication.class, args);

        new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(5);

                // Windows系统专用优化
                if (Desktop.isDesktopSupported()) {
                    Desktop.getDesktop().browse(new URI("http://localhost:8080"));
                } else {
                    Runtime.getRuntime().exec("cmd /c start http://localhost:8080");
                }

            } catch (Exception e) {
                System.err.println("⚠️ 无法自动打开浏览器，请手动访问：http://localhost:8080");
            }
        }).start();
    }
}