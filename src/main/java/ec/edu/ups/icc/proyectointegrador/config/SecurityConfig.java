package ec.edu.ups.icc.proyectointegrador.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Desactiva la protección CSRF (común para APIs REST stateless)
            .csrf(csrf -> csrf.disable())
            // Permite el acceso a todas las rutas sin autenticación
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll()
            )
            // Desactiva el formulario web HTML (formLogin)
            .formLogin(form -> form.disable())
            // Opcional: mantiene autenticación básica HTTP si la necesitas después
            .httpBasic(basic -> basic.disable());

        return http.build();
    }
}