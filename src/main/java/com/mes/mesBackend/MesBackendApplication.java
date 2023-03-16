package com.mes.mesBackend;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@EnableJpaAuditing
@SpringBootApplication
@EnableScheduling
@OpenAPIDefinition(
		servers = @Server(url = "/"),
		info = @Info(
				title = "Non Mes Backend API"
//				description = "description",
//				license = @License(name = "license name", url = "license url"),
//				contact = @Contact(url = "contract url", name = "contract name", email = "contract email")
		)
)
@SecurityScheme(
		name = AUTHORIZATION,
		type = SecuritySchemeType.APIKEY,
		in = SecuritySchemeIn.HEADER
)
public class MesBackendApplication {
	public static void main(String[] args) {
		System.setProperty("server.port", "8081");
		SpringApplication.run(MesBackendApplication.class, args);
	}
}
