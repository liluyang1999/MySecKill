package com.example.lly;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;


@SpringBootApplication
@EnableCaching
public class MySecKillApplication {

	public static void main(String[] args) {
		SpringApplication.run(MySecKillApplication.class, args);
	}


}
