package hufs.lion.team404.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

	@Bean
	public OpenAPI customOpenAPI() {
		Info info = new Info()
			.title("User Management API")
			.version("1.0")
			.description("API for managing users and their profile images.");

		return new OpenAPI()
				.info(info)
				.addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
				.addServersItem(new Server().url("/").description("Development Server"))
				.components(new io.swagger.v3.oas.models.Components()
					.addSecuritySchemes("Bearer Authentication", new SecurityScheme()
						.type(SecurityScheme.Type.HTTP)
						.scheme("bearer")
						.bearerFormat("JWT")
						.description("JWT 토큰을 입력해주세요."))
				);
	}
}
