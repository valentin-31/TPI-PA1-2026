package ar.edu.undec.adapter.service.empleado.controller;

import ar.edu.undec.adapter.service.empleado.mapper.EmpleadoWebMapper;
import ar.edu.undec.adapter.service.empleado.model.EmpleadoRequest;
import ar.edu.undec.adapter.service.empleado.model.EmpleadoResponse;
import empleado.input.BuscarEmpleadoPorLegajoInput;
import empleado.input.CrearEmpleadoInput;
import empleado.input.ListarEmpleadosInput;
import empleado.modelo.Empleado;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/*
 * Adaptador de entrada: recibe los pedidos HTTP y los traduce en llamadas a los casos de uso.
 * Depende de los PUERTOS de entrada (interfaces del core), no de las clases que los implementan.
 */
@RestController
@RequestMapping("/empleados")
public class EmpleadoController {

    private final CrearEmpleadoInput crearEmpleadoInput;
    private final BuscarEmpleadoPorLegajoInput buscarEmpleadoPorLegajoInput;
    private final ListarEmpleadosInput listarEmpleadosInput;

    public EmpleadoController(CrearEmpleadoInput crearEmpleadoInput,
                              BuscarEmpleadoPorLegajoInput buscarEmpleadoPorLegajoInput,
                              ListarEmpleadosInput listarEmpleadosInput) {
        this.crearEmpleadoInput = crearEmpleadoInput;
        this.buscarEmpleadoPorLegajoInput = buscarEmpleadoPorLegajoInput;
        this.listarEmpleadosInput = listarEmpleadosInput;
    }

    // POST /empleados
    @PostMapping
    public ResponseEntity<EmpleadoResponse> crear(@RequestBody EmpleadoRequest request) {
        Empleado creado = crearEmpleadoInput.crear(
                request.getNombreCompleto(),
                request.getLegajo(),
                request.getFechaIngreso(),
                request.getPuesto(),
                request.getSalario());

        return ResponseEntity.status(HttpStatus.CREATED).body(EmpleadoWebMapper.aResponse(creado));
    }

    // GET /empleados/{legajo}
    @GetMapping("/{legajo}")
    public ResponseEntity<EmpleadoResponse> buscarPorLegajo(@PathVariable("legajo") String legajo) {
        Empleado empleado = buscarEmpleadoPorLegajoInput.buscar(legajo);

        return ResponseEntity.ok(EmpleadoWebMapper.aResponse(empleado));
    }

    // GET /empleados
    @GetMapping
    public ResponseEntity<List<EmpleadoResponse>> listar() {
        List<Empleado> empleados = listarEmpleadosInput.listar();

        return ResponseEntity.ok(EmpleadoWebMapper.aResponse(empleados));
    }
}
