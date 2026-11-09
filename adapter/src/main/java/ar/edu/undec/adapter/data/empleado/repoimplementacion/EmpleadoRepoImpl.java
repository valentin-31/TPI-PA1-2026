package ar.edu.undec.adapter.data.empleado.repoimplementacion;

import ar.edu.undec.adapter.data.empleado.crud.EmpleadoCrud;
import ar.edu.undec.adapter.data.empleado.mapper.EmpleadoDataMapper;
import ar.edu.undec.adapter.data.empleado.model.EmpleadoEntity;
import empleado.modelo.Empleado;
import empleado.output.EmpleadoRepositorio;
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
        EmpleadoEntity guardada = empleadoCrud.save(entity);
        return EmpleadoDataMapper.aDominio(guardada);
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
