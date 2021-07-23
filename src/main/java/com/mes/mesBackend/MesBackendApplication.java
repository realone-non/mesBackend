package com.mes.mesBackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@SpringBootApplication
public class MesBackendApplication {

	public static void main(String[] args) {
		System.setProperty("server.port", "3000");
		SpringApplication.run(MesBackendApplication.class, args);
	}
}
