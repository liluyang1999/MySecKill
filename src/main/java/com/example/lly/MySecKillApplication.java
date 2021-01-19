package com.example.lly;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;

/**
 * Created by liluyang1999
 */

//@ComponentScan("com.example.lly.config")
@SpringBootApplication
@EnableCaching
@MapperScan("com.example.lly.dao.mapper")
public class MySecKillApplication {

	private static final Logger LOGGER = LoggerFactory.getLogger(MySecKillApplication.class);

	public static void main(String[] args) {
		LOGGER.info("秒杀项目开始启动");
		SpringApplication.run(MySecKillApplication.class, args);
	}


}
