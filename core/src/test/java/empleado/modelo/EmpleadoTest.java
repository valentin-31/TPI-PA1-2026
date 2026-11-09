package empleado.modelo;

import empleado.exception.EmpleadoIncompletoException;
import empleado.exception.FechaIngresoInvalidaException;
import empleado.exception.LegajoInvalidoException;
import empleado.exception.SalarioInferiorAlMinimoException;
import empleado.exception.SalarioInvalidoException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/*
 * Restricciones del enunciado y tests que las cubren:
 *
 * R1 - Todos los atributos son obligatorios
 *      -> instancia_idNulo_..., instancia_nombreCompletoNulo_..., instancia_nombreCompletoVacio_...,
 *         instancia_nombreCompletoSoloEspacios_..., instancia_legajoNulo_..., instancia_legajoVacio_...,
 *         instancia_legajoSoloEspacios_..., instancia_fechaIngresoNula_..., instancia_puestoNulo_...,
 *         instancia_salarioNulo_...
 * R2 - La fecha de ingreso no puede ser posterior a la fecha actual
 *      -> instancia_fechaIngresoHoy_..., instancia_fechaIngresoManiana_...
 * R3 - Legajo: 3 letras mayusculas seguidas de 4 numeros (ej. ABC1234)
 *      -> instancia_legajoEnMinusculas_..., instancia_legajoConMayusculasYMinusculas_...,
 *         instancia_legajoConDosLetras_..., instancia_legajoConCuatroLetras_...,
 *         instancia_legajoConTresNumeros_..., instancia_legajoConCincoNumeros_...,
 *         instancia_legajoConOrdenInvertido_..., instancia_legajoConGuion_...,
 *         instancia_legajoConEspacioAlInicio_...
 * R4 - El salario debe ser mayor a cero
 *      -> instancia_salarioCero_..., instancia_salarioNegativo_...
 * R5 - Pisos salariales por puesto (Analista 500.000, Supervisor 800.000, Gerente 1.200.000)
 *      -> instancia_salarioIgualAlMinimoDe<Puesto>_..., instancia_salarioMenorAlMinimoDe<Puesto>_...,
 *         instancia_salarioSobreElPisoDeAnalista_..., instancia_salarioValidoParaAnalistaPeroNoParaSupervisor_...
 */
@DisplayName("Entidad Empleado")
class EmpleadoTest {

    private static final UUID ID = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
    private static final String NOMBRE_COMPLETO = "Guillermo Farre";
    private static final String LEGAJO = "ABC1234";
    private static final LocalDate FECHA_INGRESO = LocalDate.of(2011, 6, 26);
    private static final Puesto PUESTO = Puesto.ANALISTA;
    private static final BigDecimal SALARIO = new BigDecimal("650000");

    // ---------- Camino feliz ----------

    @Test
    @DisplayName("Crea el empleado con todos sus atributos cuando los datos son validos")
    void instancia_datosValidos_creaEmpleadoConTodosSusAtributos() {
        // Arrange - Act
        Empleado empleado = Empleado.instancia(ID, NOMBRE_COMPLETO, LEGAJO, FECHA_INGRESO, PUESTO, SALARIO);

        // Assert
        assertEquals(ID, empleado.getId());
        assertEquals(NOMBRE_COMPLETO, empleado.getNombreCompleto());
        assertEquals(LEGAJO, empleado.getLegajo());
        assertEquals(FECHA_INGRESO, empleado.getFechaIngreso());
        assertEquals(PUESTO, empleado.getPuesto());
        assertEquals(SALARIO, empleado.getSalario());
    }

    // ---------- R1: atributos obligatorios ----------

    @Test
    @DisplayName("Falla si el id es nulo")
    void instancia_idNulo_lanzaEmpleadoIncompletoException() {
        assertThrows(EmpleadoIncompletoException.class,
                () -> Empleado.instancia(null, NOMBRE_COMPLETO, LEGAJO, FECHA_INGRESO, PUESTO, SALARIO));
    }

    @Test
    @DisplayName("Falla si el nombre completo es nulo")
    void instancia_nombreCompletoNulo_lanzaEmpleadoIncompletoException() {
        assertThrows(EmpleadoIncompletoException.class,
                () -> Empleado.instancia(ID, null, LEGAJO, FECHA_INGRESO, PUESTO, SALARIO));
    }

    @Test
    @DisplayName("Falla si el nombre completo esta vacio")
    void instancia_nombreCompletoVacio_lanzaEmpleadoIncompletoException() {
        assertThrows(EmpleadoIncompletoException.class,
                () -> Empleado.instancia(ID, "", LEGAJO, FECHA_INGRESO, PUESTO, SALARIO));
    }

    @Test
    @DisplayName("Falla si el nombre completo tiene solo espacios")
    void instancia_nombreCompletoSoloEspacios_lanzaEmpleadoIncompletoException() {
        assertThrows(EmpleadoIncompletoException.class,
                () -> Empleado.instancia(ID, "   ", LEGAJO, FECHA_INGRESO, PUESTO, SALARIO));
    }

    @Test
    @DisplayName("Falla si el legajo es nulo")
    void instancia_legajoNulo_lanzaEmpleadoIncompletoException() {
        assertThrows(EmpleadoIncompletoException.class,
                () -> Empleado.instancia(ID, NOMBRE_COMPLETO, null, FECHA_INGRESO, PUESTO, SALARIO));
    }

    @Test
    @DisplayName("Falla si el legajo esta vacio")
    void instancia_legajoVacio_lanzaEmpleadoIncompletoException() {
        assertThrows(EmpleadoIncompletoException.class,
                () -> Empleado.instancia(ID, NOMBRE_COMPLETO, "", FECHA_INGRESO, PUESTO, SALARIO));
    }

    @Test
    @DisplayName("Falla si el legajo tiene solo espacios")
    void instancia_legajoSoloEspacios_lanzaEmpleadoIncompletoException() {
        assertThrows(EmpleadoIncompletoException.class,
                () -> Empleado.instancia(ID, NOMBRE_COMPLETO, "   ", FECHA_INGRESO, PUESTO, SALARIO));
    }

    @Test
    @DisplayName("Falla si la fecha de ingreso es nula")
    void instancia_fechaIngresoNula_lanzaEmpleadoIncompletoException() {
        assertThrows(EmpleadoIncompletoException.class,
                () -> Empleado.instancia(ID, NOMBRE_COMPLETO, LEGAJO, null, PUESTO, SALARIO));
    }

    @Test
    @DisplayName("Falla si el puesto es nulo")
    void instancia_puestoNulo_lanzaEmpleadoIncompletoException() {
        assertThrows(EmpleadoIncompletoException.class,
                () -> Empleado.instancia(ID, NOMBRE_COMPLETO, LEGAJO, FECHA_INGRESO, null, SALARIO));
    }

    @Test
    @DisplayName("Falla si el salario es nulo")
    void instancia_salarioNulo_lanzaEmpleadoIncompletoException() {
        assertThrows(EmpleadoIncompletoException.class,
                () -> Empleado.instancia(ID, NOMBRE_COMPLETO, LEGAJO, FECHA_INGRESO, PUESTO, null));
    }

    // ---------- R2: fecha de ingreso ----------

    @Test
    @DisplayName("Acepta como fecha de ingreso el dia de hoy")
    void instancia_fechaIngresoHoy_creaEmpleado() {
        // Borde valido: hoy no es posterior a hoy
        LocalDate hoy = LocalDate.now();

        assertDoesNotThrow(
                () -> Empleado.instancia(ID, NOMBRE_COMPLETO, LEGAJO, hoy, PUESTO, SALARIO));
    }

    @Test
    @DisplayName("Falla si la fecha de ingreso es maniana")
    void instancia_fechaIngresoManiana_lanzaFechaIngresoInvalidaException() {
        // Borde invalido: maniana ya es fecha futura
        LocalDate maniana = LocalDate.now().plusDays(1);

        assertThrows(FechaIngresoInvalidaException.class,
                () -> Empleado.instancia(ID, NOMBRE_COMPLETO, LEGAJO, maniana, PUESTO, SALARIO));
    }

    // ---------- R3: formato de legajo ----------

    @Test
    @DisplayName("Falla si el legajo esta en minusculas (abc1234)")
    void instancia_legajoEnMinusculas_lanzaLegajoInvalidoException() {
        assertThrows(LegajoInvalidoException.class,
                () -> Empleado.instancia(ID, NOMBRE_COMPLETO, "abc1234", FECHA_INGRESO, PUESTO, SALARIO));
    }

    @Test
    @DisplayName("Falla si el legajo mezcla mayusculas y minusculas (AbC1234)")
    void instancia_legajoConMayusculasYMinusculas_lanzaLegajoInvalidoException() {
        assertThrows(LegajoInvalidoException.class,
                () -> Empleado.instancia(ID, NOMBRE_COMPLETO, "AbC1234", FECHA_INGRESO, PUESTO, SALARIO));
    }

    @Test
    @DisplayName("Falla si el legajo tiene 2 letras (AB1234)")
    void instancia_legajoConDosLetras_lanzaLegajoInvalidoException() {
        assertThrows(LegajoInvalidoException.class,
                () -> Empleado.instancia(ID, NOMBRE_COMPLETO, "AB1234", FECHA_INGRESO, PUESTO, SALARIO));
    }

    @Test
    @DisplayName("Falla si el legajo tiene 4 letras (ABCD1234)")
    void instancia_legajoConCuatroLetras_lanzaLegajoInvalidoException() {
        assertThrows(LegajoInvalidoException.class,
                () -> Empleado.instancia(ID, NOMBRE_COMPLETO, "ABCD1234", FECHA_INGRESO, PUESTO, SALARIO));
    }

    @Test
    @DisplayName("Falla si el legajo tiene 3 numeros (ABC123)")
    void instancia_legajoConTresNumeros_lanzaLegajoInvalidoException() {
        assertThrows(LegajoInvalidoException.class,
                () -> Empleado.instancia(ID, NOMBRE_COMPLETO, "ABC123", FECHA_INGRESO, PUESTO, SALARIO));
    }

    @Test
    @DisplayName("Falla si el legajo tiene 5 numeros (ABC12345)")
    void instancia_legajoConCincoNumeros_lanzaLegajoInvalidoException() {
        assertThrows(LegajoInvalidoException.class,
                () -> Empleado.instancia(ID, NOMBRE_COMPLETO, "ABC12345", FECHA_INGRESO, PUESTO, SALARIO));
    }

    @Test
    @DisplayName("Falla si el legajo tiene los numeros antes que las letras (1234ABC)")
    void instancia_legajoConOrdenInvertido_lanzaLegajoInvalidoException() {
        assertThrows(LegajoInvalidoException.class,
                () -> Empleado.instancia(ID, NOMBRE_COMPLETO, "1234ABC", FECHA_INGRESO, PUESTO, SALARIO));
    }

    @Test
    @DisplayName("Falla si el legajo tiene un guion (ABC-1234)")
    void instancia_legajoConGuion_lanzaLegajoInvalidoException() {
        assertThrows(LegajoInvalidoException.class,
                () -> Empleado.instancia(ID, NOMBRE_COMPLETO, "ABC-1234", FECHA_INGRESO, PUESTO, SALARIO));
    }

    @Test
    @DisplayName("Falla si el legajo empieza con un espacio")
    void instancia_legajoConEspacioAlInicio_lanzaLegajoInvalidoException() {
        assertThrows(LegajoInvalidoException.class,
                () -> Empleado.instancia(ID, NOMBRE_COMPLETO, " ABC1234", FECHA_INGRESO, PUESTO, SALARIO));
    }

    // ---------- R4: salario mayor a cero ----------

    @Test
    @DisplayName("Falla si el salario es cero")
    void instancia_salarioCero_lanzaSalarioInvalidoException() {
        assertThrows(SalarioInvalidoException.class,
                () -> Empleado.instancia(ID, NOMBRE_COMPLETO, LEGAJO, FECHA_INGRESO, PUESTO, BigDecimal.ZERO));
    }

    @Test
    @DisplayName("Falla si el salario es negativo")
    void instancia_salarioNegativo_lanzaSalarioInvalidoException() {
        BigDecimal salarioNegativo = new BigDecimal("-1");

        assertThrows(SalarioInvalidoException.class,
                () -> Empleado.instancia(ID, NOMBRE_COMPLETO, LEGAJO, FECHA_INGRESO, PUESTO, salarioNegativo));
    }

    // ---------- R5: pisos salariales por puesto ----------

    @Test
    @DisplayName("Acepta un Analista con el salario minimo exacto (500.000)")
    void instancia_salarioIgualAlMinimoDeAnalista_creaEmpleado() {
        // Borde valido: exactamente el piso
        BigDecimal salarioMinimo = new BigDecimal("500000");

        Empleado empleado = Empleado.instancia(ID, NOMBRE_COMPLETO, LEGAJO, FECHA_INGRESO, Puesto.ANALISTA, salarioMinimo);

        assertEquals(salarioMinimo, empleado.getSalario());
    }

    @Test
    @DisplayName("Falla si un Analista cobra un centavo menos que el minimo (499.999,99)")
    void instancia_salarioMenorAlMinimoDeAnalista_lanzaSalarioInferiorAlMinimoException() {
        // Borde invalido: un centavo por debajo del piso
        BigDecimal salario = new BigDecimal("499999.99");

        assertThrows(SalarioInferiorAlMinimoException.class,
                () -> Empleado.instancia(ID, NOMBRE_COMPLETO, LEGAJO, FECHA_INGRESO, Puesto.ANALISTA, salario));
    }

    @Test
    @DisplayName("Acepta un Supervisor con el salario minimo exacto (800.000)")
    void instancia_salarioIgualAlMinimoDeSupervisor_creaEmpleado() {
        BigDecimal salarioMinimo = new BigDecimal("800000");

        Empleado empleado = Empleado.instancia(ID, NOMBRE_COMPLETO, LEGAJO, FECHA_INGRESO, Puesto.SUPERVISOR, salarioMinimo);

        assertEquals(salarioMinimo, empleado.getSalario());
    }

    @Test
    @DisplayName("Falla si un Supervisor cobra un centavo menos que el minimo (799.999,99)")
    void instancia_salarioMenorAlMinimoDeSupervisor_lanzaSalarioInferiorAlMinimoException() {
        BigDecimal salario = new BigDecimal("799999.99");

        assertThrows(SalarioInferiorAlMinimoException.class,
                () -> Empleado.instancia(ID, NOMBRE_COMPLETO, LEGAJO, FECHA_INGRESO, Puesto.SUPERVISOR, salario));
    }

    @Test
    @DisplayName("Acepta un Gerente con el salario minimo exacto (1.200.000)")
    void instancia_salarioIgualAlMinimoDeGerente_creaEmpleado() {
        BigDecimal salarioMinimo = new BigDecimal("1200000");

        Empleado empleado = Empleado.instancia(ID, NOMBRE_COMPLETO, LEGAJO, FECHA_INGRESO, Puesto.GERENTE, salarioMinimo);

        assertEquals(salarioMinimo, empleado.getSalario());
    }

    @Test
    @DisplayName("Falla si un Gerente cobra un centavo menos que el minimo (1.199.999,99)")
    void instancia_salarioMenorAlMinimoDeGerente_lanzaSalarioInferiorAlMinimoException() {
        BigDecimal salario = new BigDecimal("1199999.99");

        assertThrows(SalarioInferiorAlMinimoException.class,
                () -> Empleado.instancia(ID, NOMBRE_COMPLETO, LEGAJO, FECHA_INGRESO, Puesto.GERENTE, salario));
    }

    @Test
    @DisplayName("Acepta un Analista con 600.000 (supera su piso)")
    void instancia_salarioSobreElPisoDeAnalista_creaEmpleado() {
        // El piso depende del puesto: 600.000 es valido para Analista
        BigDecimal salario = new BigDecimal("600000");

        assertDoesNotThrow(
                () -> Empleado.instancia(ID, NOMBRE_COMPLETO, LEGAJO, FECHA_INGRESO, Puesto.ANALISTA, salario));
    }

    @Test
    @DisplayName("Falla si un Supervisor cobra 600.000 (valido para Analista, no para Supervisor)")
    void instancia_salarioValidoParaAnalistaPeroNoParaSupervisor_lanzaSalarioInferiorAlMinimoException() {
        // Mismo salario que el test anterior, pero con Supervisor no alcanza el piso de 800.000
        BigDecimal salario = new BigDecimal("600000");

        assertThrows(SalarioInferiorAlMinimoException.class,
                () -> Empleado.instancia(ID, NOMBRE_COMPLETO, LEGAJO, FECHA_INGRESO, Puesto.SUPERVISOR, salario));
    }
}
