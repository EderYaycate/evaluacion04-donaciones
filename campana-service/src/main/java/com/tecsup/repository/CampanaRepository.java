package com.tecsup.repository;

import com.tecsup.model.Campana;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CampanaRepository extends JpaRepository<Campana, Long> {

    boolean existsByNombre(String nombre);

    boolean existsByNombreAndIdNot(String nombre, Long id);
}
