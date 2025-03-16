package com.shiyue.codeparse;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author 24226
 */
@SpringBootApplication
@EnableFeignClients
@ComponentScan(basePackages = {"com.shiyue.codeparse", "com.shiyue.common"})
@MapperScan(basePackages = {"com.shiyue.codeparse.demospring.dao"})
public class CodeParseApplication {

	public static void main(String[] args) {
		SpringApplication.run(CodeParseApplication.class, args);
	}

}