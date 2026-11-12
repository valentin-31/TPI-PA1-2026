package empleado.usecase;

import empleado.exception.EmpleadoNoEncontradoException;
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
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

/*
 * Caso de uso: Buscar Empleado por Legajo
 *
 * Caminos y tests que los cubren:
 *  - El legajo existe    -> retorna el empleado              -> buscar_legajoExistente_...
 *  - El legajo no existe -> EmpleadoNoEncontradoException    -> buscar_legajoInexistente_...
 */
@DisplayName("Caso de uso Buscar Empleado por Legajo")
@ExtendWith(MockitoExtension.class)
class BuscarEmpleadoPorLegajoUseCaseTest {

    private static final String LEGAJO = "ABC1234";

    @Mock
    private EmpleadoRepositorio empleadoRepositorio;

    @Test
    @DisplayName("Retorna el empleado cuando el legajo existe")
    void buscar_legajoExistente_retornaEmpleado() {
        // Arrange
        Empleado empleado = Empleado.instancia(
                UUID.fromString("123e4567-e89b-12d3-a456-426614174000"),
                "Guillermo Farre", LEGAJO, LocalDate.of(2011, 6, 26), Puesto.ANALISTA, new BigDecimal("650000"));
        when(empleadoRepositorio.buscarPorLegajo(LEGAJO)).thenReturn(Optional.of(empleado));
        BuscarEmpleadoPorLegajoUseCase buscarEmpleadoPorLegajoUseCase =
                new BuscarEmpleadoPorLegajoUseCase(empleadoRepositorio);

        // Act
        Empleado resultado = buscarEmpleadoPorLegajoUseCase.buscar(LEGAJO);

        // Assert
        assertSame(empleado, resultado);
    }

    @Test
    @DisplayName("Falla con EmpleadoNoEncontradoException cuando el legajo no existe")
    void buscar_legajoInexistente_lanzaEmpleadoNoEncontradoException() {
        // Arrange
        when(empleadoRepositorio.buscarPorLegajo(LEGAJO)).thenReturn(Optional.empty());
        BuscarEmpleadoPorLegajoUseCase buscarEmpleadoPorLegajoUseCase =
                new BuscarEmpleadoPorLegajoUseCase(empleadoRepositorio);

        // Act - Assert
        assertThrows(EmpleadoNoEncontradoException.class,
                () -> buscarEmpleadoPorLegajoUseCase.buscar(LEGAJO));
    }
}
