package ec.edu.ups.icc.proyectointegrador.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        // Definimos el nombre del esquema de seguridad
        final String securitySchemeName = "bearerAuth";
        
        return new OpenAPI()
                // Informacin general de tu API
                .info(new Info()
                        .title("API de Gestión de Eventos Académicos")
                        .version("1.0")
                        .description("Documentación oficial de los endpoints del proyecto."))
                
                //Le decimos a Swagger que todas las rutas requieren este esquema por defecto
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                
                //Configuramos cómo funciona el candado (JWT Bearer)
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName, new SecurityScheme()
                                .name(securitySchemeName)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")));
    }
}