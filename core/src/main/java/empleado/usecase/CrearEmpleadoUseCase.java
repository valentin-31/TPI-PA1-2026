package empleado.usecase;

import empleado.exception.EmpleadoExisteException;
import empleado.input.CrearEmpleadoInput;
import empleado.modelo.Empleado;
import empleado.modelo.Puesto;
import empleado.output.EmpleadoRepositorio;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class CrearEmpleadoUseCase implements CrearEmpleadoInput {

    private final EmpleadoRepositorio empleadoRepositorio;

    public CrearEmpleadoUseCase(EmpleadoRepositorio empleadoRepositorio) {
        this.empleadoRepositorio = empleadoRepositorio;
    }

    @Override
    public Empleado crear(String nombreCompleto, String legajo, LocalDate fechaIngreso,
                          Puesto puesto, BigDecimal salario) {
        // 1) Crear la entidad: si algun dato es invalido, se corta aca y nunca llega al repositorio
        Empleado empleado = Empleado.instancia(UUID.randomUUID(), nombreCompleto, legajo, fechaIngreso, puesto, salario);

        // 2) Regla que necesita consultar afuera: el legajo no puede repetirse
        if (empleadoRepositorio.existePorLegajo(legajo)) {
            throw new EmpleadoExisteException("Ya existe un empleado con el legajo " + legajo);
        }

        // 3) Guardar y retornar lo que devuelve el repositorio
        return empleadoRepositorio.guardar(empleado);
    }
}
