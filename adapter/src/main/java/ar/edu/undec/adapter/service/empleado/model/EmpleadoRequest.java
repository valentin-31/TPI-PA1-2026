package ar.edu.undec.adapter.service.empleado.model;

import empleado.modelo.Puesto;

import java.math.BigDecimal;
import java.time.LocalDate;

/*
 * DTO de ENTRADA: la forma del JSON que llega en POST /empleados.
 * No tiene id porque lo genera el caso de uso.
 * Jackson usa el constructor vacio y los setters para llenarlo a partir del JSON.
 */
public class EmpleadoRequest {

    private String nombreCompleto;
    private String legajo;
    private LocalDate fechaIngreso;
    private Puesto puesto;
    private BigDecimal salario;

    public EmpleadoRequest() {
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getLegajo() {
        return legajo;
    }

    public void setLegajo(String legajo) {
        this.legajo = legajo;
    }

    public LocalDate getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(LocalDate fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public Puesto getPuesto() {
        return puesto;
    }

    public void setPuesto(Puesto puesto) {
        this.puesto = puesto;
    }

    public BigDecimal getSalario() {
        return salario;
    }

    public void setSalario(BigDecimal salario) {
        this.salario = salario;
    }
}
