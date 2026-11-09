package empleado.usecase;

import empleado.modelo.Empleado;
import empleado.modelo.Puesto;
import empleado.output.EmpleadoRepositorio;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

/*
 * Caso de uso: Listar Empleados (necesario para GET /empleados)
 *
 * Caminos y tests que los cubren:
 *  - Hay empleados registrados -> retorna todos   -> listar_hayEmpleados_...
 *  - No hay empleados          -> lista vacia,
 *                                 sin excepcion   -> listar_sinEmpleados_...
 */
@DisplayName("Caso de uso Listar Empleados")
@ExtendWith(MockitoExtension.class)
class ListarEmpleadosUseCaseTest {

    @Mock
    private EmpleadoRepositorio empleadoRepositorio;

    @Test
    @DisplayName("Retorna todos los empleados registrados")
    void listar_hayEmpleados_retornaTodos() {
        // Arrange
        Empleado farre = Empleado.instancia(
                UUID.fromString("123e4567-e89b-12d3-a456-426614174000"),
                "Guillermo Farre", "ABC1234", LocalDate.of(2011, 6, 26), Puesto.ANALISTA, new BigDecimal("650000"));
        Empleado olave = Empleado.instancia(
                UUID.fromString("223e4567-e89b-12d3-a456-426614174000"),
                "Juan Carlos Olave", "XYZ5678", LocalDate.of(2011, 6, 26), Puesto.GERENTE, new BigDecimal("1500000"));
        List<Empleado> empleados = List.of(farre, olave);
        when(empleadoRepositorio.buscarTodos()).thenReturn(empleados);
        ListarEmpleadosUseCase listarEmpleadosUseCase = new ListarEmpleadosUseCase(empleadoRepositorio);

        // Act
        List<Empleado> resultado = listarEmpleadosUseCase.listar();

        // Assert
        assertEquals(2, resultado.size());
        assertEquals(empleados, resultado);
    }

    @Test
    @DisplayName("Retorna una lista vacia (sin excepcion) cuando no hay empleados")
    void listar_sinEmpleados_retornaListaVacia() {
        // Arrange
        when(empleadoRepositorio.buscarTodos()).thenReturn(List.of());
        ListarEmpleadosUseCase listarEmpleadosUseCase = new ListarEmpleadosUseCase(empleadoRepositorio);

        // Act
        List<Empleado> resultado = listarEmpleadosUseCase.listar();

        // Assert
        assertTrue(resultado.isEmpty());
    }
}
