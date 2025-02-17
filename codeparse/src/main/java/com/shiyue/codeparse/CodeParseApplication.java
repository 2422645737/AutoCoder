package com.shiyue.codeparse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author 24226
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.shiyue.codeparse", "com.shiyue.common"})
@EnableFeignClients
public class CodeParseApplication {

	public static void main(String[] args) {
		SpringApplication.run(CodeParseApplication.class, args);
	}

}