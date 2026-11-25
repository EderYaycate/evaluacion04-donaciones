package com.tecsup.repository;

import com.tecsup.model.Donacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DonacionRepository extends JpaRepository<Donacion, Long> {

    List<Donacion> findByCampanaId(Long campanaId);

    List<Donacion> findByDonanteId(Long donanteId);
}
