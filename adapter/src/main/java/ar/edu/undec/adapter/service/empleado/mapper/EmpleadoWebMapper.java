package ar.edu.undec.adapter.service.empleado.mapper;

import ar.edu.undec.adapter.service.empleado.model.EmpleadoResponse;
import empleado.modelo.Empleado;

import java.util.ArrayList;
import java.util.List;

/*
 * Traduce la entidad de dominio al DTO que se devuelve como JSON.
 */
public final class EmpleadoWebMapper {

    private EmpleadoWebMapper() {
        // Clase utilitaria: solo metodos estaticos, no se instancia
    }

    public static EmpleadoResponse aResponse(Empleado empleado) {
        return new EmpleadoResponse(
                empleado.getId(),
                empleado.getNombreCompleto(),
                empleado.getLegajo(),
                empleado.getFechaIngreso(),
                empleado.getPuesto(),
                empleado.getSalario());
    }

    public static List<EmpleadoResponse> aResponse(List<Empleado> empleados) {
        List<EmpleadoResponse> responses = new ArrayList<>();
        for (Empleado empleado : empleados) {
            responses.add(aResponse(empleado));
        }
        return responses;
    }
}
