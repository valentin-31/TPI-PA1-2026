package empleado.usecase;

import empleado.input.ListarEmpleadosInput;
import empleado.modelo.Empleado;
import empleado.output.EmpleadoRepositorio;

import java.util.List;

public class ListarEmpleadosUseCase implements ListarEmpleadosInput {

    private final EmpleadoRepositorio empleadoRepositorio;

    public ListarEmpleadosUseCase(EmpleadoRepositorio empleadoRepositorio) {
        this.empleadoRepositorio = empleadoRepositorio;
    }

    @Override
    public List<Empleado> listar() {
        return empleadoRepositorio.buscarTodos();
    }
}
