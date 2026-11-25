package com.tecsup.services;

import com.tecsup.client.DonanteClient;
import com.tecsup.dto.DonanteDTO;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Tolerancia a fallos sobre donante-service.
 * Si ese microservicio se cae, la donacion se sigue devolviendo con datos
 * minimos en vez de romper toda la respuesta.
 */
@Service
public class DonanteCircuitService {

    private static final Logger log = LoggerFactory.getLogger(DonanteCircuitService.class);

    @Autowired
    private DonanteClient donanteClient;

    @CircuitBreaker(name = "donanteService", fallbackMethod = "donanteFallback")
    public DonanteDTO obtenerDonante(Long id) {
        log.info("Llamando a donante-service para el donante {}", id);
        return donanteClient.obtenerDonante(id);
    }

    public DonanteDTO donanteFallback(Long id, Exception ex) {
        log.warn("Circuit Breaker activado para donante-service: {}", ex.getMessage());
        DonanteDTO dto = new DonanteDTO();
        dto.setId(id);
        dto.setNombres("Donante no disponible");
        dto.setApellidos("");
        return dto;
    }
}
