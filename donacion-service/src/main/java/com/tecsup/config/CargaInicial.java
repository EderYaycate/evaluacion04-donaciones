package com.tecsup.config;

import com.tecsup.model.Donacion;
import com.tecsup.repository.DonacionRepository;
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
    CommandLineRunner cargarDonaciones(DonacionRepository repo) {
        return args -> {
            if (repo.count() > 0) {
                return;
            }
            repo.save(crear(1L, 1L, "250.00", "2026-11-05", "YAPE"));
            repo.save(crear(2L, 1L, "500.00", "2026-11-08", "TRANSFERENCIA"));
            repo.save(crear(3L, 2L, "1200.50", "2026-10-20", "TARJETA"));
            repo.save(crear(1L, 2L, "300.00", "2026-11-12", "EFECTIVO"));
            log.info("Carga inicial: {} donaciones", repo.count());
        };
    }

    private Donacion crear(Long donanteId, Long campanaId, String monto,
                           String fecha, String metodoPago) {
        Donacion d = new Donacion();
        d.setDonanteId(donanteId);
        d.setCampanaId(campanaId);
        d.setMonto(new BigDecimal(monto));
        d.setFechaDonacion(LocalDate.parse(fecha));
        d.setMetodoPago(metodoPago);
        return d;
    }
}
