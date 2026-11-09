package empleado.output;

import empleado.modelo.Empleado;

public interface EmpleadoRepositorio {

    boolean existePorLegajo(String legajo);

    Empleado guardar(Empleado empleado);
}
