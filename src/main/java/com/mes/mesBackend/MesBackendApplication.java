package com.mes.mesBackend;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

import static io.swagger.v3.oas.annotations.enums.SecuritySchemeIn.HEADER;
import static io.swagger.v3.oas.annotations.enums.SecuritySchemeType.APIKEY;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@EnableJpaAuditing
@SpringBootApplication
@EnableScheduling
@OpenAPIDefinition(servers = @Server(url = "/"))
@SecurityScheme(
		name = AUTHORIZATION,
		type = APIKEY,
		in = HEADER
)
public class MesBackendApplication {

	public static void main(String[] args) {
		System.setProperty("server.port", "8081");
		SpringApplication.run(MesBackendApplication.class, args);
	}
}
