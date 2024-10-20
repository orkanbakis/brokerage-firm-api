package com.brokeragefirm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class BrokerageFirmApplication {

  public static void main(String[] args) {
    SpringApplication.run(BrokerageFirmApplication.class, args);
  }

}
