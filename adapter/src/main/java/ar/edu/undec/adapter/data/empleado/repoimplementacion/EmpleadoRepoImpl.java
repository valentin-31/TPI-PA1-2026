package ar.edu.undec.adapter.data.empleado.repoimplementacion;

import ar.edu.undec.adapter.data.empleado.crud.EmpleadoCrud;
import ar.edu.undec.adapter.data.empleado.mapper.EmpleadoDataMapper;
import ar.edu.undec.adapter.data.empleado.model.EmpleadoEntity;
import empleado.exception.EmpleadoExisteException;
import empleado.modelo.Empleado;
import empleado.output.EmpleadoRepositorio;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/*
 * Adaptador de salida: implementa el puerto EmpleadoRepositorio del core usando Spring Data JPA.
 */
@Repository
public class EmpleadoRepoImpl implements EmpleadoRepositorio {

    private final EmpleadoCrud empleadoCrud;

    // Spring ve que el constructor necesita un EmpleadoCrud y se lo inyecta
    public EmpleadoRepoImpl(EmpleadoCrud empleadoCrud) {
        this.empleadoCrud = empleadoCrud;
    }

    @Override
    public boolean existePorLegajo(String legajo) {
        return empleadoCrud.existsByLegajo(legajo);
    }

    @Override
    public Empleado guardar(Empleado empleado) {
        EmpleadoEntity entity = EmpleadoDataMapper.aEntity(empleado);

        try {
            // saveAndFlush ejecuta el INSERT en este momento: si la base lo rechaza, el error salta aca adentro
            EmpleadoEntity guardada = empleadoCrud.saveAndFlush(entity);
            return EmpleadoDataMapper.aDominio(guardada);
        } catch (DataIntegrityViolationException excepcion) {
            // Dos altas simultaneas con el mismo legajo pueden pasar las dos el "existePorLegajo" del caso de uso.
            // La restriccion UNIQUE de la base frena a la segunda: la traducimos a la excepcion del dominio (409).
            if (esLegajoDuplicado(excepcion)) {
                throw new EmpleadoExisteException("Ya existe un empleado con el legajo " + empleado.getLegajo());
            }
            throw excepcion;
        }
    }

    private boolean esLegajoDuplicado(DataIntegrityViolationException excepcion) {
        String mensajeDeLaBase = excepcion.getMostSpecificCause().getMessage();
        return mensajeDeLaBase != null
                && mensajeDeLaBase.toLowerCase().contains(EmpleadoEntity.RESTRICCION_LEGAJO_UNICO);
    }

    @Override
    public Optional<Empleado> buscarPorLegajo(String legajo) {
        Optional<EmpleadoEntity> resultado = empleadoCrud.findByLegajo(legajo);

        if (resultado.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(EmpleadoDataMapper.aDominio(resultado.get()));
    }

    @Override
    public List<Empleado> buscarTodos() {
        List<Empleado> empleados = new ArrayList<>();
        for (EmpleadoEntity entity : empleadoCrud.findAll()) {
            empleados.add(EmpleadoDataMapper.aDominio(entity));
        }
        return empleados;
    }
}
