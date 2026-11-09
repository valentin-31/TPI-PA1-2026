package ar.edu.undec.adapter.service.empleado.model;

import empleado.modelo.Puesto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

/*
 * DTO de SALIDA: la forma del JSON que se devuelve al cliente.
 * Jackson usa los getters para armar el JSON (cada getXxx se convierte en el campo "xxx").
 */
public class EmpleadoResponse {

    private final UUID id;
    private final String nombreCompleto;
    private final String legajo;
    private final LocalDate fechaIngreso;
    private final Puesto puesto;
    private final BigDecimal salario;

    public EmpleadoResponse(UUID id, String nombreCompleto, String legajo,
                            LocalDate fechaIngreso, Puesto puesto, BigDecimal salario) {
        this.id = id;
        this.nombreCompleto = nombreCompleto;
        this.legajo = legajo;
        this.fechaIngreso = fechaIngreso;
        this.puesto = puesto;
        this.salario = salario;
    }

    public UUID getId() {
        return id;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public String getLegajo() {
        return legajo;
    }

    public LocalDate getFechaIngreso() {
        return fechaIngreso;
    }

    public Puesto getPuesto() {
        return puesto;
    }

    public BigDecimal getSalario() {
        return salario;
    }
}
