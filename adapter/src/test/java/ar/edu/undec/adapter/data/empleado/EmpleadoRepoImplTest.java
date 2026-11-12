package ar.edu.undec.adapter.data.empleado;

import ar.edu.undec.adapter.data.empleado.repoimplementacion.EmpleadoRepoImpl;
import empleado.exception.EmpleadoExisteException;
import empleado.modelo.Empleado;
import empleado.modelo.Puesto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/*
 * Test de INTEGRACION del adaptador de persistencia.
 * No hay mocks: se guarda y se lee de verdad en una base H2 en memoria.
 *
 * Metodos del puerto EmpleadoRepositorio y tests que los cubren:
 *  - guardar          -> guardar_empleadoValido_..., guardar_legajoYaGuardadoConOtroId_...
 *  - buscarPorLegajo  -> buscarPorLegajo_legajoGuardado_..., buscarPorLegajo_legajoInexistente_...
 *  - existePorLegajo  -> existePorLegajo_legajoGuardado_..., existePorLegajo_legajoInexistente_...
 *  - buscarTodos      -> buscarTodos_conDosEmpleadosGuardados_..., buscarTodos_sinEmpleados_...
 */
@DisplayName("Adaptador de persistencia EmpleadoRepoImpl")
@DataJpaTest
@Import(EmpleadoRepoImpl.class)
class EmpleadoRepoImplTest {

    @Autowired
    private EmpleadoRepoImpl empleadoRepoImpl;

    @Autowired
    private TestEntityManager entityManager;

    private final Empleado farre = Empleado.instancia(
            UUID.fromString("123e4567-e89b-12d3-a456-426614174000"),
            "Guillermo Farre", "ABC1234", LocalDate.of(2011, 6, 26), Puesto.ANALISTA, new BigDecimal("650000"));

    private final Empleado olave = Empleado.instancia(
            UUID.fromString("223e4567-e89b-12d3-a456-426614174000"),
            "Juan Carlos Olave", "XYZ5678", LocalDate.of(2011, 6, 26), Puesto.GERENTE, new BigDecimal("1500000"));

    // ---------- guardar ----------

    @Test
    @DisplayName("Guarda el empleado y lo retorna con los mismos datos")
    void guardar_empleadoValido_persisteYRetornaConLosMismosDatos() {
        // Act
        Empleado guardado = empleadoRepoImpl.guardar(farre);

        // Assert
        verificarMismosDatos(farre, guardado);
    }

    @Test
    @DisplayName("Lanza EmpleadoExisteException si la base rechaza el legajo por duplicado (altas simultaneas)")
    void guardar_legajoYaGuardadoConOtroId_lanzaEmpleadoExisteException() {
        // Arrange: simula dos altas simultaneas. Las dos pasaron el "existePorLegajo" del caso de uso
        // antes de que la otra guardara, asi que llegan las dos al repositorio con el mismo legajo.
        empleadoRepoImpl.guardar(farre);
        Empleado otroConElMismoLegajo = Empleado.instancia(
                UUID.fromString("323e4567-e89b-12d3-a456-426614174000"),
                "Juan Carlos Olave", "ABC1234", LocalDate.of(2011, 6, 26), Puesto.GERENTE, new BigDecimal("1500000"));

        // Act - Assert: la restriccion UNIQUE de la base lo frena y el adaptador lo traduce a la excepcion del dominio
        assertThrows(EmpleadoExisteException.class,
                () -> empleadoRepoImpl.guardar(otroConElMismoLegajo));
    }

    // ---------- buscarPorLegajo ----------

    @Test
    @DisplayName("Recupera de la base un empleado guardado buscandolo por legajo")
    void buscarPorLegajo_legajoGuardado_retornaEmpleadoConLosMismosDatos() {
        // Arrange
        empleadoRepoImpl.guardar(farre);
        limpiarCache();

        // Act
        Optional<Empleado> resultado = empleadoRepoImpl.buscarPorLegajo("ABC1234");

        // Assert
        assertTrue(resultado.isPresent());
        verificarMismosDatos(farre, resultado.get());
    }

    @Test
    @DisplayName("Retorna un Optional vacio si el legajo no esta guardado")
    void buscarPorLegajo_legajoInexistente_retornaOptionalVacio() {
        // Act
        Optional<Empleado> resultado = empleadoRepoImpl.buscarPorLegajo("ZZZ9999");

        // Assert
        assertTrue(resultado.isEmpty());
    }

    // ---------- existePorLegajo ----------

    @Test
    @DisplayName("Indica que existe un legajo guardado")
    void existePorLegajo_legajoGuardado_retornaTrue() {
        // Arrange
        empleadoRepoImpl.guardar(farre);
        limpiarCache();

        // Act - Assert
        assertTrue(empleadoRepoImpl.existePorLegajo("ABC1234"));
    }

    @Test
    @DisplayName("Indica que no existe un legajo que nunca se guardo")
    void existePorLegajo_legajoInexistente_retornaFalse() {
        assertFalse(empleadoRepoImpl.existePorLegajo("ZZZ9999"));
    }

    // ---------- buscarTodos ----------

    @Test
    @DisplayName("Retorna todos los empleados guardados")
    void buscarTodos_conDosEmpleadosGuardados_retornaLosDos() {
        // Arrange
        empleadoRepoImpl.guardar(farre);
        empleadoRepoImpl.guardar(olave);
        limpiarCache();

        // Act
        List<Empleado> resultado = empleadoRepoImpl.buscarTodos();

        // Assert
        assertEquals(2, resultado.size());
        List<String> legajos = resultado.stream().map(Empleado::getLegajo).toList();
        assertTrue(legajos.contains("ABC1234"));
        assertTrue(legajos.contains("XYZ5678"));
    }

    @Test
    @DisplayName("Retorna una lista vacia si no hay empleados guardados")
    void buscarTodos_sinEmpleados_retornaListaVacia() {
        assertTrue(empleadoRepoImpl.buscarTodos().isEmpty());
    }

    // ---------- Auxiliares ----------

    /**
     * Fuerza a escribir en la base (flush) y vacia la memoria de JPA (clear),
     * para que la siguiente lectura vaya de verdad a la base y no a la cache.
     */
    private void limpiarCache() {
        entityManager.flush();
        entityManager.clear();
    }

    private void verificarMismosDatos(Empleado esperado, Empleado actual) {
        assertEquals(esperado.getId(), actual.getId());
        assertEquals(esperado.getNombreCompleto(), actual.getNombreCompleto());
        assertEquals(esperado.getLegajo(), actual.getLegajo());
        assertEquals(esperado.getFechaIngreso(), actual.getFechaIngreso());
        assertEquals(esperado.getPuesto(), actual.getPuesto());
        // compareTo y no equals: la base puede devolver 650000.00 en lugar de 650000
        assertEquals(0, esperado.getSalario().compareTo(actual.getSalario()));
    }
}
