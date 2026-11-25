package com.tecsup.client;

import com.tecsup.dto.CampanaDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "campana-service")
public interface CampanaClient {

    @GetMapping("/api/campanas/{id}")
    CampanaDTO obtenerCampana(@PathVariable("id") Long id);
}
