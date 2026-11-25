package com.tecsup.services;

import com.tecsup.dto.DonacionDTO;
import com.tecsup.exception.ResourceNotFoundException;
import com.tecsup.model.Donacion;
import com.tecsup.repository.DonacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DonacionService {

    @Autowired
    private DonacionRepository repo;

    @Autowired
    private DonanteCircuitService donanteCircuitService;

    @Autowired
    private CampanaCircuitService campanaCircuitService;

    public List<DonacionDTO> listar() {
        return repo.findAll().stream().map(this::aDTOCompleto).toList();
    }

    public List<DonacionDTO> listarPorCampana(Long campanaId) {
        return repo.findByCampanaId(campanaId).stream().map(this::aDTOCompleto).toList();
    }

    public DonacionDTO obtener(Long id) {
        return aDTOCompleto(buscar(id));
    }

    public DonacionDTO crear(DonacionDTO dto) {
        Donacion donacion = new Donacion();
        copiar(dto, donacion);
        return aDTOCompleto(repo.save(donacion));
    }

    public DonacionDTO actualizar(Long id, DonacionDTO dto) {
        Donacion donacion = buscar(id);
        copiar(dto, donacion);
        return aDTOCompleto(repo.save(donacion));
    }

    public void eliminar(Long id) {
        repo.delete(buscar(id));
    }

    private Donacion buscar(Long id) {
        return repo.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("No existe la donacion con id " + id));
    }

    private void copiar(DonacionDTO dto, Donacion donacion) {
        donacion.setDonanteId(dto.getDonanteId());
        donacion.setCampanaId(dto.getCampanaId());
        donacion.setMonto(dto.getMonto());
        donacion.setFechaDonacion(dto.getFechaDonacion());
        donacion.setMetodoPago(dto.getMetodoPago());
    }

    /** Aqui ocurre la integracion: la donacion se completa con datos de los otros dos servicios. */
    private DonacionDTO aDTOCompleto(Donacion donacion) {
        DonacionDTO dto = new DonacionDTO();
        dto.setId(donacion.getId());
        dto.setDonanteId(donacion.getDonanteId());
        dto.setCampanaId(donacion.getCampanaId());
        dto.setMonto(donacion.getMonto());
        dto.setFechaDonacion(donacion.getFechaDonacion());
        dto.setMetodoPago(donacion.getMetodoPago());
        dto.setDonante(donanteCircuitService.obtenerDonante(donacion.getDonanteId()));
        dto.setCampana(campanaCircuitService.obtenerCampana(donacion.getCampanaId()));
        return dto;
    }
}
