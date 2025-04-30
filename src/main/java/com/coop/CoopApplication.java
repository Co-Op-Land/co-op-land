package com.coop;

import com.coop.global.security.JwtSecurityProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@SpringBootApplication
@EnableJpaAuditing
@EnableMethodSecurity(securedEnabled = true)
@EnableConfigurationProperties({JwtSecurityProperties.class})
public class CoopApplication {

	public static void main(String[] args) {
		SpringApplication.run(CoopApplication.class, args);
	}
}
