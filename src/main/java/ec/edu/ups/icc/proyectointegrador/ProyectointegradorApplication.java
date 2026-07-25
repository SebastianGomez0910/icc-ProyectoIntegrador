package ec.edu.ups.icc.proyectointegrador;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ProyectointegradorApplication {

    public static void main(String[] args) {
        // Carga .env y setea las propiedades en memoria antes de levantar Spring
        Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
        dotenv.entries().forEach(entry -> {
            System.setProperty(entry.getKey(), entry.getValue());
        });

        SpringApplication.run(ProyectointegradorApplication.class, args);
    }

}