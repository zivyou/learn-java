package com.zivyou.learntimerangecrud;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = {"com.zivyou.learntimerangecrud.mapper"})
public class LearnTimeRangeCrudApplication {

	public static void main(String[] args) {
		SpringApplication.run(LearnTimeRangeCrudApplication.class, args);
	}

}
