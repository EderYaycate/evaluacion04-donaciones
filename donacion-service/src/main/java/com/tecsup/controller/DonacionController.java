package com.tecsup.controller;

import com.tecsup.dto.DonacionDTO;
import com.tecsup.services.DonacionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/donaciones")
public class DonacionController {

    @Autowired
    private DonacionService service;

    @GetMapping
    public ResponseEntity<List<DonacionDTO>> listar(
            @RequestParam(required = false) Long campanaId) {
        return ResponseEntity.ok(
                campanaId == null ? service.listar() : service.listarPorCampana(campanaId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DonacionDTO> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtener(id));
    }

    @PostMapping
    public ResponseEntity<DonacionDTO> crear(@Valid @RequestBody DonacionDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DonacionDTO> actualizar(@PathVariable Long id,
                                                  @Valid @RequestBody DonacionDTO dto) {
        return ResponseEntity.ok(service.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
