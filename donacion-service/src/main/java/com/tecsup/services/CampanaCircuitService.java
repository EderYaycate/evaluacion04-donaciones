package com.tecsup.services;

import com.tecsup.client.CampanaClient;
import com.tecsup.dto.CampanaDTO;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CampanaCircuitService {

    private static final Logger log = LoggerFactory.getLogger(CampanaCircuitService.class);

    @Autowired
    private CampanaClient campanaClient;

    @CircuitBreaker(name = "campanaService", fallbackMethod = "campanaFallback")
    public CampanaDTO obtenerCampana(Long id) {
        log.info("Llamando a campana-service para la campana {}", id);
        return campanaClient.obtenerCampana(id);
    }

    public CampanaDTO campanaFallback(Long id, Exception ex) {
        log.warn("Circuit Breaker activado para campana-service: {}", ex.getMessage());
        CampanaDTO dto = new CampanaDTO();
        dto.setId(id);
        dto.setNombre("Campana no disponible");
        dto.setEstado("DESCONOCIDO");
        return dto;
    }
}
