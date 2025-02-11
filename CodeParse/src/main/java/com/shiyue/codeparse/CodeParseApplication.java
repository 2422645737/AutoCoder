package com.shiyue.codeparse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.shiyue.codeparse", "com.shiyue.common"})
public class CodeParseApplication {

	public static void main(String[] args) {
		SpringApplication.run(CodeParseApplication.class, args);
	}

}
