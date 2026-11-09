package ar.edu.undec.adapter.data.empleado.model;

import empleado.modelo.Puesto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "empleados",
        uniqueConstraints = @UniqueConstraint(name = EmpleadoEntity.RESTRICCION_LEGAJO_UNICO, columnNames = "legajo"))
public class EmpleadoEntity {

    // Nombre de la restriccion UNIQUE del legajo. Se usa para reconocerla cuando la base rechaza un alta.
    public static final String RESTRICCION_LEGAJO_UNICO = "uk_empleado_legajo";

    @Id
    private UUID id;

    @Column(name = "nombre_completo", nullable = false)
    private String nombreCompleto;

    @Column(nullable = false, length = 7)
    private String legajo;

    @Column(name = "fecha_ingreso", nullable = false)
    private LocalDate fechaIngreso;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Puesto puesto;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal salario;

    // JPA exige un constructor sin parametros para poder crear el objeto al leer de la base
    public EmpleadoEntity() {
    }

    public EmpleadoEntity(UUID id, String nombreCompleto, String legajo,
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

    public void setId(UUID id) {
        this.id = id;
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
