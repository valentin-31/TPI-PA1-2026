package ar.edu.undec.adapter.data.empleado.mapper;

import ar.edu.undec.adapter.data.empleado.model.EmpleadoEntity;
import empleado.modelo.Empleado;

/*
 * Traduce entre la entidad de dominio (core) y la entidad de base de datos (JPA).
 */
public final class EmpleadoDataMapper {

    private EmpleadoDataMapper() {
        // Clase utilitaria: solo metodos estaticos, no se instancia
    }

    // Dominio -> Base (para guardar)
    public static EmpleadoEntity aEntity(Empleado empleado) {
        return new EmpleadoEntity(
                empleado.getId(),
                empleado.getNombreCompleto(),
                empleado.getLegajo(),
                empleado.getFechaIngreso(),
                empleado.getPuesto(),
                empleado.getSalario());
    }

    // Base -> Dominio (al leer). Pasa por el factory method, asi el dato se vuelve a validar.
    public static Empleado aDominio(EmpleadoEntity entity) {
        return Empleado.instancia(
                entity.getId(),
                entity.getNombreCompleto(),
                entity.getLegajo(),
                entity.getFechaIngreso(),
                entity.getPuesto(),
                entity.getSalario());
    }
}
