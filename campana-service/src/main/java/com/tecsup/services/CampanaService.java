package com.tecsup.services;

import com.tecsup.dto.CampanaDTO;
import com.tecsup.exception.BusinessRuleException;
import com.tecsup.exception.DuplicateResourceException;
import com.tecsup.exception.ResourceNotFoundException;
import com.tecsup.model.Campana;
import com.tecsup.repository.CampanaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CampanaService {

    @Autowired
    private CampanaRepository repo;

    public List<CampanaDTO> listar() {
        return repo.findAll().stream().map(this::aDTO).toList();
    }

    public CampanaDTO obtener(Long id) {
        return aDTO(buscar(id));
    }

    public CampanaDTO crear(CampanaDTO dto) {
        validarFechas(dto);
        if (repo.existsByNombre(dto.getNombre())) {
            throw new DuplicateResourceException("Ya existe una campana llamada " + dto.getNombre());
        }
        Campana campana = new Campana();
        copiar(dto, campana);
        return aDTO(repo.save(campana));
    }

    public CampanaDTO actualizar(Long id, CampanaDTO dto) {
        validarFechas(dto);
        Campana campana = buscar(id);
        if (repo.existsByNombreAndIdNot(dto.getNombre(), id)) {
            throw new DuplicateResourceException("Otra campana ya usa el nombre " + dto.getNombre());
        }
        copiar(dto, campana);
        return aDTO(repo.save(campana));
    }

    public void eliminar(Long id) {
        repo.delete(buscar(id));
    }

    /** Regla de negocio que una anotacion por campo no puede comprobar. */
    private void validarFechas(CampanaDTO dto) {
        if (dto.getFechaInicio() != null && dto.getFechaFin() != null
                && dto.getFechaFin().isBefore(dto.getFechaInicio())) {
            throw new BusinessRuleException("La fecha de fin no puede ser anterior a la de inicio");
        }
    }

    private Campana buscar(Long id) {
        return repo.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("No existe la campana con id " + id));
    }

    private void copiar(CampanaDTO dto, Campana campana) {
        campana.setNombre(dto.getNombre());
        campana.setDescripcion(dto.getDescripcion());
        campana.setMetaRecaudacion(dto.getMetaRecaudacion());
        campana.setFechaInicio(dto.getFechaInicio());
        campana.setFechaFin(dto.getFechaFin());
        campana.setEstado(dto.getEstado() == null ? "ACTIVA" : dto.getEstado());
    }

    private CampanaDTO aDTO(Campana campana) {
        CampanaDTO dto = new CampanaDTO();
        dto.setId(campana.getId());
        dto.setNombre(campana.getNombre());
        dto.setDescripcion(campana.getDescripcion());
        dto.setMetaRecaudacion(campana.getMetaRecaudacion());
        dto.setFechaInicio(campana.getFechaInicio());
        dto.setFechaFin(campana.getFechaFin());
        dto.setEstado(campana.getEstado());
        return dto;
    }
}
