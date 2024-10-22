package com.ithe.tushu_guanli_xitong;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@SpringBootApplication
@ServletComponentScan
@EnableTransactionManagement
public class TushuGuanliXitongApplication {

    public static void main(String[] args) {
        SpringApplication.run(TushuGuanliXitongApplication.class, args);
    }



}
