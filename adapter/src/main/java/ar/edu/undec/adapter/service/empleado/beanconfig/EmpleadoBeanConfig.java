package ar.edu.undec.adapter.service.empleado.beanconfig;

import empleado.input.BuscarEmpleadoPorLegajoInput;
import empleado.input.CrearEmpleadoInput;
import empleado.input.ListarEmpleadosInput;
import empleado.output.EmpleadoRepositorio;
import empleado.usecase.BuscarEmpleadoPorLegajoUseCase;
import empleado.usecase.CrearEmpleadoUseCase;
import empleado.usecase.ListarEmpleadosUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/*
 * Registra los casos de uso del core como beans de Spring.
 *
 * El core no conoce Spring (sus clases no tienen @Service ni @Component), asi que Spring
 * no puede descubrirlos solo. Aca le decimos como crearlos y con que repositorio:
 * Spring busca un bean que implemente EmpleadoRepositorio (EmpleadoRepoImpl) y lo pasa por parametro.
 */
@Configuration
public class EmpleadoBeanConfig {

    @Bean
    public CrearEmpleadoInput crearEmpleadoInput(EmpleadoRepositorio empleadoRepositorio) {
        return new CrearEmpleadoUseCase(empleadoRepositorio);
    }

    @Bean
    public BuscarEmpleadoPorLegajoInput buscarEmpleadoPorLegajoInput(EmpleadoRepositorio empleadoRepositorio) {
        return new BuscarEmpleadoPorLegajoUseCase(empleadoRepositorio);
    }

    @Bean
    public ListarEmpleadosInput listarEmpleadosInput(EmpleadoRepositorio empleadoRepositorio) {
        return new ListarEmpleadosUseCase(empleadoRepositorio);
    }
}
