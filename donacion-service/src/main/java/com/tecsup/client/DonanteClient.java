package com.tecsup.client;

import com.tecsup.dto.DonanteDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/** El nombre es el que donante-service registro en Eureka, no una URL fija. */
@FeignClient(name = "donante-service")
public interface DonanteClient {

    @GetMapping("/api/donantes/{id}")
    DonanteDTO obtenerDonante(@PathVariable("id") Long id);
}
