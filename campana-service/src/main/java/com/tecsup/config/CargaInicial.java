package com.tecsup.config;

import com.tecsup.model.Campana;
import com.tecsup.repository.CampanaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.math.BigDecimal;
import java.time.LocalDate;

@Configuration
@Profile("dev")
public class CargaInicial {

    private static final Logger log = LoggerFactory.getLogger(CargaInicial.class);

    @Bean
    CommandLineRunner cargarCampanas(CampanaRepository repo) {
        return args -> {
            if (repo.count() > 0) {
                return;
            }
            repo.save(crear("Navidad Solidaria 2026",
                    "Juguetes y alimentos para ninos de Santa Anita",
                    "15000.00", "2026-11-01", "2026-12-24", "ACTIVA"));
            repo.save(crear("Techo Digno",
                    "Materiales de construccion para familias afectadas por lluvias",
                    "42000.00", "2026-09-15", "2026-12-31", "ACTIVA"));
            repo.save(crear("Becas Tecsup",
                    "Fondo de becas para estudiantes de bajos recursos",
                    "80000.00", "2026-03-01", "2026-10-31", "CERRADA"));
            log.info("Carga inicial: {} campanas", repo.count());
        };
    }

    private Campana crear(String nombre, String descripcion, String meta,
                          String inicio, String fin, String estado) {
        Campana c = new Campana();
        c.setNombre(nombre);
        c.setDescripcion(descripcion);
        c.setMetaRecaudacion(new BigDecimal(meta));
        c.setFechaInicio(LocalDate.parse(inicio));
        c.setFechaFin(LocalDate.parse(fin));
        c.setEstado(estado);
        return c;
    }
}
