package ar.edu.undec.adapter;

import empleado.input.BuscarEmpleadoPorLegajoInput;
import empleado.input.CrearEmpleadoInput;
import empleado.input.ListarEmpleadosInput;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/*
 * Test de INTEGRACION de punta a punta: levanta la aplicacion completa (con H2), sin mocks.
 * Un pedido HTTP recorre controller -> caso de uso -> entidad -> repositorio -> base de datos.
 *
 * Verifica que todas las piezas esten conectadas (en particular, que los casos de uso del core,
 * que no tienen anotaciones de Spring, esten registrados como beans).
 *
 *  - El contexto arranca con los casos de uso   -> contexto_arranca_...
 *  - Crear y luego buscar el mismo empleado     -> crearYBuscar_...
 *  - Legajo duplicado detectado por la base     -> crear_legajoDuplicado_...
 *  - Regla de dominio violada                   -> crear_legajoInvalido_...
 */
@DisplayName("Integracion completa de la API de empleados")
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class EmpleadoIntegracionTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CrearEmpleadoInput crearEmpleadoInput;

    @Autowired
    private BuscarEmpleadoPorLegajoInput buscarEmpleadoPorLegajoInput;

    @Autowired
    private ListarEmpleadosInput listarEmpleadosInput;

    private static final String JSON_FARRE = """
            {
              "nombreCompleto": "Guillermo Farre",
              "legajo": "ABC1234",
              "fechaIngreso": "2011-06-26",
              "puesto": "ANALISTA",
              "salario": 650000
            }
            """;

    @Test
    @DisplayName("La aplicacion arranca y los casos de uso del core estan disponibles como beans")
    void contexto_arranca_casosDeUsoDisponibles() {
        assertNotNull(crearEmpleadoInput);
        assertNotNull(buscarEmpleadoPorLegajoInput);
        assertNotNull(listarEmpleadosInput);
    }

    @Test
    @DisplayName("Un empleado creado por POST se puede recuperar por GET")
    void crearYBuscar_empleadoValido_seGuardaYSeRecupera() throws Exception {
        // Crear
        mockMvc.perform(post("/empleados").contentType(MediaType.APPLICATION_JSON).content(JSON_FARRE))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.legajo").value("ABC1234"));

        // Buscar por legajo: viene de la base
        mockMvc.perform(get("/empleados/ABC1234"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombreCompleto").value("Guillermo Farre"))
                .andExpect(jsonPath("$.puesto").value("ANALISTA"));

        // Listar: aparece en el listado
        mockMvc.perform(get("/empleados"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].legajo").value("ABC1234"));
    }

    @Test
    @DisplayName("Crear dos veces el mismo legajo responde 409 la segunda vez")
    void crear_legajoDuplicado_responde409() throws Exception {
        mockMvc.perform(post("/empleados").contentType(MediaType.APPLICATION_JSON).content(JSON_FARRE))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/empleados").contentType(MediaType.APPLICATION_JSON).content(JSON_FARRE))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.mensaje").value("Ya existe un empleado con el legajo ABC1234"));
    }

    @Test
    @DisplayName("Crear con un legajo mal formado responde 400 (lo rechaza la entidad real)")
    void crear_legajoInvalido_responde400() throws Exception {
        String json = """
                {
                  "nombreCompleto": "Guillermo Farre",
                  "legajo": "abc",
                  "fechaIngreso": "2011-06-26",
                  "puesto": "ANALISTA",
                  "salario": 650000
                }
                """;

        mockMvc.perform(post("/empleados").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensaje").exists());
    }
}
