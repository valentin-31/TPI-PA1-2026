package empleado.output;

import empleado.modelo.Empleado;

import java.util.Optional;

public interface EmpleadoRepositorio {

    boolean existePorLegajo(String legajo);

    Empleado guardar(Empleado empleado);

    Optional<Empleado> buscarPorLegajo(String legajo);
}
