package com.app.bankX;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
		info = @Info(
				title = "BankX App",
				description = "Backend REST APIs for BankX",
				version = "v1.0",
				contact = @Contact(
						name = "Ashika Drolia",
						email = "ashikadrolia2008@gmail.com",
						url  =  "https://github.com/ashikad/bankX"
				),
				license = @License(
						name = "Ashika Drolia",
						url =  "https://github.com/ashikad"
				)
		),
		externalDocs = @ExternalDocumentation(
				description = "BankX app documentation",
				url = "https://github.com/ashikad/bankX"
		)
)
public class BankXApplication {

	public static void main(String[] args) {
		SpringApplication.run(BankXApplication.class, args);
	}

}
