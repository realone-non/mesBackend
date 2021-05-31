package com.mes.mesBackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication
public class MesBackendApplication {

	public static void main(String[] args) {

		Test test = new Test();
		test.setData("test");
		String data = test.getData();
		System.out.println("data = " + data);

		SpringApplication.run(MesBackendApplication.class, args);
	}
}
