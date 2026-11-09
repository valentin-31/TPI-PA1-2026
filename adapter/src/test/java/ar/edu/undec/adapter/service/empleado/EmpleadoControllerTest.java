package ar.edu.undec.adapter.service.empleado;

import ar.edu.undec.adapter.service.empleado.controller.EmpleadoController;
import empleado.input.BuscarEmpleadoPorLegajoInput;
import empleado.input.CrearEmpleadoInput;
import empleado.input.ListarEmpleadosInput;
import empleado.modelo.Empleado;
import empleado.modelo.Puesto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/*
 * Test del adaptador web (controller REST) con MockMvc.
 * Los casos de uso son mocks: aca se prueba la capa HTTP (rutas, JSON y codigos de estado),
 * no la logica de negocio, que ya esta testeada en el core.
 *
 * Endpoints y tests que los cubren (caminos felices):
 *  - POST /empleados           -> crear_datosValidos_...
 *  - GET  /empleados/{legajo}  -> buscar_legajoExistente_...
 *  - GET  /empleados           -> listar_hayEmpleados_..., listar_sinEmpleados_...
 */
@DisplayName("Adaptador web EmpleadoController")
@WebMvcTest(EmpleadoController.class)
class EmpleadoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CrearEmpleadoInput crearEmpleadoInput;

    @MockBean
    private BuscarEmpleadoPorLegajoInput buscarEmpleadoPorLegajoInput;

    @MockBean
    private ListarEmpleadosInput listarEmpleadosInput;

    private final Empleado farre = Empleado.instancia(
            UUID.fromString("123e4567-e89b-12d3-a456-426614174000"),
            "Guillermo Farre", "ABC1234", LocalDate.of(2011, 6, 26), Puesto.ANALISTA, new BigDecimal("650000"));

    private final Empleado olave = Empleado.instancia(
            UUID.fromString("223e4567-e89b-12d3-a456-426614174000"),
            "Juan Carlos Olave", "XYZ5678", LocalDate.of(2011, 6, 26), Puesto.GERENTE, new BigDecimal("1500000"));

    // ---------- POST /empleados ----------

    @Test
    @DisplayName("POST /empleados con datos validos responde 201 y el empleado creado")
    void crear_datosValidos_responde201ConElEmpleadoCreado() throws Exception {
        // Arrange: el mock solo responde si el JSON se convirtio exactamente en estos valores
        when(crearEmpleadoInput.crear("Guillermo Farre", "ABC1234", LocalDate.of(2011, 6, 26),
                Puesto.ANALISTA, new BigDecimal("650000"))).thenReturn(farre);

        String json = """
                {
                  "nombreCompleto": "Guillermo Farre",
                  "legajo": "ABC1234",
                  "fechaIngreso": "2011-06-26",
                  "puesto": "ANALISTA",
                  "salario": 650000
                }
                """;

        // Act - Assert
        mockMvc.perform(post("/empleados")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("123e4567-e89b-12d3-a456-426614174000"))
                .andExpect(jsonPath("$.nombreCompleto").value("Guillermo Farre"))
                .andExpect(jsonPath("$.legajo").value("ABC1234"))
                .andExpect(jsonPath("$.fechaIngreso").value("2011-06-26"))
                .andExpect(jsonPath("$.puesto").value("ANALISTA"))
                .andExpect(jsonPath("$.salario").value(650000));
    }

    // ---------- GET /empleados/{legajo} ----------

    @Test
    @DisplayName("GET /empleados/{legajo} con legajo existente responde 200 y el empleado")
    void buscar_legajoExistente_responde200ConElEmpleado() throws Exception {
        // Arrange
        when(buscarEmpleadoPorLegajoInput.buscar("ABC1234")).thenReturn(farre);

        // Act - Assert
        mockMvc.perform(get("/empleados/ABC1234"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("123e4567-e89b-12d3-a456-426614174000"))
                .andExpect(jsonPath("$.nombreCompleto").value("Guillermo Farre"))
                .andExpect(jsonPath("$.legajo").value("ABC1234"))
                .andExpect(jsonPath("$.fechaIngreso").value("2011-06-26"))
                .andExpect(jsonPath("$.puesto").value("ANALISTA"))
                .andExpect(jsonPath("$.salario").value(650000));
    }

    // ---------- GET /empleados ----------

    @Test
    @DisplayName("GET /empleados responde 200 y la lista de empleados")
    void listar_hayEmpleados_responde200ConLaLista() throws Exception {
        // Arrange
        when(listarEmpleadosInput.listar()).thenReturn(List.of(farre, olave));

        // Act - Assert
        mockMvc.perform(get("/empleados"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].legajo").value("ABC1234"))
                .andExpect(jsonPath("$[0].nombreCompleto").value("Guillermo Farre"))
                .andExpect(jsonPath("$[1].legajo").value("XYZ5678"))
                .andExpect(jsonPath("$[1].nombreCompleto").value("Juan Carlos Olave"));
    }

    @Test
    @DisplayName("GET /empleados sin empleados responde 200 y una lista vacia")
    void listar_sinEmpleados_responde200ConListaVacia() throws Exception {
        // Arrange
        when(listarEmpleadosInput.listar()).thenReturn(List.of());

        // Act - Assert
        mockMvc.perform(get("/empleados"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }
}
