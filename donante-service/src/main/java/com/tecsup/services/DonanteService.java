package com.tecsup.services;

import com.tecsup.dto.DonanteDTO;
import com.tecsup.exception.DuplicateResourceException;
import com.tecsup.exception.ResourceNotFoundException;
import com.tecsup.model.Donante;
import com.tecsup.repository.DonanteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DonanteService {

    @Autowired
    private DonanteRepository repo;

    public List<DonanteDTO> listar() {
        return repo.findAll().stream().map(this::aDTO).toList();
    }

    public DonanteDTO obtener(Long id) {
        return aDTO(buscar(id));
    }

    public DonanteDTO crear(DonanteDTO dto) {
        if (repo.existsByDni(dto.getDni())) {
            throw new DuplicateResourceException("Ya existe un donante con el DNI " + dto.getDni());
        }
        Donante donante = new Donante();
        copiar(dto, donante);
        return aDTO(repo.save(donante));
    }

    public DonanteDTO actualizar(Long id, DonanteDTO dto) {
        Donante donante = buscar(id);
        if (repo.existsByDniAndIdNot(dto.getDni(), id)) {
            throw new DuplicateResourceException("Otro donante ya usa el DNI " + dto.getDni());
        }
        copiar(dto, donante);
        return aDTO(repo.save(donante));
    }

    public void eliminar(Long id) {
        repo.delete(buscar(id));
    }

    private Donante buscar(Long id) {
        return repo.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("No existe el donante con id " + id));
    }

    private void copiar(DonanteDTO dto, Donante donante) {
        donante.setNombres(dto.getNombres());
        donante.setApellidos(dto.getApellidos());
        donante.setDni(dto.getDni());
        donante.setEmail(dto.getEmail());
        donante.setTelefono(dto.getTelefono());
        donante.setActivo(dto.getActivo() == null || dto.getActivo());
    }

    private DonanteDTO aDTO(Donante donante) {
        DonanteDTO dto = new DonanteDTO();
        dto.setId(donante.getId());
        dto.setNombres(donante.getNombres());
        dto.setApellidos(donante.getApellidos());
        dto.setDni(donante.getDni());
        dto.setEmail(donante.getEmail());
        dto.setTelefono(donante.getTelefono());
        dto.setActivo(donante.getActivo());
        return dto;
    }
}
