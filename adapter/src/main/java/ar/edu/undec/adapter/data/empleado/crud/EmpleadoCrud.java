package ar.edu.undec.adapter.data.empleado.crud;

import ar.edu.undec.adapter.data.empleado.model.EmpleadoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

/*
 * Spring Data JPA genera la implementacion de esta interfaz al arrancar.
 * - save, findAll, findById, etc. vienen heredados de JpaRepository.
 * - existsByLegajo y findByLegajo se arman leyendo el nombre del metodo.
 */
public interface EmpleadoCrud extends JpaRepository<EmpleadoEntity, UUID> {

    boolean existsByLegajo(String legajo);

    Optional<EmpleadoEntity> findByLegajo(String legajo);
}
