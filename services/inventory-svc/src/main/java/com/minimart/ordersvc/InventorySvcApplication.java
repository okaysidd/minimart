package com.minimart.ordersvc;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class InventorySvcApplication {

    public static void main(String[] args) {
        SpringApplication.run(InventorySvcApplication.class, args);
    }
}
