package empleado.usecase;

import empleado.exception.EmpleadoNoEncontradoException;
import empleado.input.BuscarEmpleadoPorLegajoInput;
import empleado.modelo.Empleado;
import empleado.output.EmpleadoRepositorio;

import java.util.Optional;

public class BuscarEmpleadoPorLegajoUseCase implements BuscarEmpleadoPorLegajoInput {

    private final EmpleadoRepositorio empleadoRepositorio;

    public BuscarEmpleadoPorLegajoUseCase(EmpleadoRepositorio empleadoRepositorio) {
        this.empleadoRepositorio = empleadoRepositorio;
    }

    @Override
    public Empleado buscar(String legajo) {
        Optional<Empleado> resultado = empleadoRepositorio.buscarPorLegajo(legajo);

        if (resultado.isEmpty()) {
            throw new EmpleadoNoEncontradoException("No existe un empleado con el legajo " + legajo);
        }

        return resultado.get();
    }
}
