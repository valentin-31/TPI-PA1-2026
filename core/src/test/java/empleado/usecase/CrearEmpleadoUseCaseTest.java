package empleado.usecase;

import empleado.exception.EmpleadoExisteException;
import empleado.exception.LegajoInvalidoException;
import empleado.modelo.Empleado;
import empleado.modelo.Puesto;
import empleado.output.EmpleadoRepositorio;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/*
 * Caso de uso: Crear Empleado
 *
 * Caminos y tests que los cubren:
 *  - Datos validos y legajo libre -> guarda y retorna el empleado   -> crear_datosValidosYLegajoLibre_...
 *  - Legajo ya registrado         -> EmpleadoExisteException        -> crear_legajoExistente_...
 *  - Datos invalidos              -> excepcion de la entidad, sin
 *                                    tocar el repositorio           -> crear_legajoConFormatoInvalido_...
 */
@DisplayName("Caso de uso Crear Empleado")
@ExtendWith(MockitoExtension.class)
class CrearEmpleadoUseCaseTest {

    private static final String NOMBRE_COMPLETO = "Guillermo Farre";
    private static final String LEGAJO = "ABC1234";
    private static final LocalDate FECHA_INGRESO = LocalDate.of(2011, 6, 26);
    private static final Puesto PUESTO = Puesto.ANALISTA;
    private static final BigDecimal SALARIO = new BigDecimal("650000");

    @Mock
    private EmpleadoRepositorio empleadoRepositorio;

    @Test
    @DisplayName("Guarda y retorna el empleado cuando los datos son validos y el legajo no existe")
    void crear_datosValidosYLegajoLibre_guardaYRetornaEmpleado() {
        // Arrange
        Empleado empleadoGuardado = Empleado.instancia(
                UUID.fromString("123e4567-e89b-12d3-a456-426614174000"),
                NOMBRE_COMPLETO, LEGAJO, FECHA_INGRESO, PUESTO, SALARIO);
        when(empleadoRepositorio.existePorLegajo(LEGAJO)).thenReturn(false);
        when(empleadoRepositorio.guardar(any(Empleado.class))).thenReturn(empleadoGuardado);
        CrearEmpleadoUseCase crearEmpleadoUseCase = new CrearEmpleadoUseCase(empleadoRepositorio);

        // Act
        Empleado resultado = crearEmpleadoUseCase.crear(NOMBRE_COMPLETO, LEGAJO, FECHA_INGRESO, PUESTO, SALARIO);

        // Assert: retorna lo que devolvio el repositorio
        assertSame(empleadoGuardado, resultado);

        // Assert: el empleado que se mando a guardar tiene id generado y los datos recibidos
        ArgumentCaptor<Empleado> captor = ArgumentCaptor.forClass(Empleado.class);
        verify(empleadoRepositorio).guardar(captor.capture());
        Empleado empleadoEnviado = captor.getValue();
        assertNotNull(empleadoEnviado.getId());
        assertEquals(NOMBRE_COMPLETO, empleadoEnviado.getNombreCompleto());
        assertEquals(LEGAJO, empleadoEnviado.getLegajo());
        assertEquals(FECHA_INGRESO, empleadoEnviado.getFechaIngreso());
        assertEquals(PUESTO, empleadoEnviado.getPuesto());
        assertEquals(SALARIO, empleadoEnviado.getSalario());
    }

    @Test
    @DisplayName("Falla con EmpleadoExisteException y no guarda si el legajo ya esta registrado")
    void crear_legajoExistente_lanzaEmpleadoExisteExceptionYNoGuarda() {
        // Arrange
        when(empleadoRepositorio.existePorLegajo(LEGAJO)).thenReturn(true);
        CrearEmpleadoUseCase crearEmpleadoUseCase = new CrearEmpleadoUseCase(empleadoRepositorio);

        // Act - Assert
        assertThrows(EmpleadoExisteException.class,
                () -> crearEmpleadoUseCase.crear(NOMBRE_COMPLETO, LEGAJO, FECHA_INGRESO, PUESTO, SALARIO));
        verify(empleadoRepositorio, never()).guardar(any(Empleado.class));
    }

    @Test
    @DisplayName("Falla con la excepcion de la entidad y no consulta ni guarda si los datos son invalidos")
    void crear_legajoConFormatoInvalido_lanzaLegajoInvalidoExceptionYNoUsaElRepositorio() {
        // Arrange: no se programa el mock porque el flujo nunca deberia llegar al repositorio
        CrearEmpleadoUseCase crearEmpleadoUseCase = new CrearEmpleadoUseCase(empleadoRepositorio);

        // Act - Assert
        assertThrows(LegajoInvalidoException.class,
                () -> crearEmpleadoUseCase.crear(NOMBRE_COMPLETO, "abc1234", FECHA_INGRESO, PUESTO, SALARIO));
        verify(empleadoRepositorio, never()).existePorLegajo(anyString());
        verify(empleadoRepositorio, never()).guardar(any(Empleado.class));
    }
}
