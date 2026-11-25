package com.tecsup.config;

import com.tecsup.model.Donante;
import com.tecsup.repository.DonanteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/** Datos de ejemplo. Solo se insertan si la tabla esta vacia y solo en el perfil dev. */
@Configuration
@Profile("dev")
public class CargaInicial {

    private static final Logger log = LoggerFactory.getLogger(CargaInicial.class);

    @Bean
    CommandLineRunner cargarDonantes(DonanteRepository repo) {
        return args -> {
            if (repo.count() > 0) {
                return;
            }
            repo.save(crear("Eder Marcelo", "Yaycate Bardales", "72345678",
                    "eder.yaycate@tecsup.edu.pe", "987654321"));
            repo.save(crear("Lucia", "Ramos Vega", "71234567",
                    "lucia.ramos@correo.com", "961234567"));
            repo.save(crear("Carlos", "Paredes Soto", "70123456",
                    "carlos.paredes@correo.com", "955112233"));
            repo.save(crear("Ana Maria", "Flores Quispe", "73456789",
                    "ana.flores@correo.com", null));
            log.info("Carga inicial: {} donantes", repo.count());
        };
    }

    private Donante crear(String nombres, String apellidos, String dni,
                          String email, String telefono) {
        Donante d = new Donante();
        d.setNombres(nombres);
        d.setApellidos(apellidos);
        d.setDni(dni);
        d.setEmail(email);
        d.setTelefono(telefono);
        d.setActivo(true);
        return d;
    }
}
