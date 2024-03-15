package com.sampledashboard1.config.security.swagger;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                contact = @Contact(
                        name = "Darshan",
                        email = "darshans@techhive.co.in"
                ),
                description = "Welcome to the SimpleDashboard project! This project is built using Spring Boot and Java 17, with a SQL Server database.",
                title = "SimpleDashboard1 Project API",
                version = "1.0"
//                license = @License(
//                        name = "Licence name",
//                        url = "https://some-url.com"
//                ),
//                termsOfService = "Terms of service"
        ),
        servers = {
               /* @Server(
                        description = "Local ENV",
                        url = "http://192.168.2.167:8000/"
                ),
                @Server(
                        description = "Dev ENV",
                        url = "http://192.168.2.55:8000/"
                ),*/
                @Server(
                        description = "local",
                        url = "http://localhost:9003/"
                )
        },
        security = {
                @SecurityRequirement(
                        name = "bearerAuth"
                )
        }
)
@SecurityScheme(
        name = "bearerAuth",
        description = "JWT auth description",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
public class OpenApiConfig {
}
