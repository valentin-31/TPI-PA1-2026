package empleado.modelo;

import empleado.exception.EmpleadoIncompletoException;
import empleado.exception.FechaIngresoInvalidaException;
import empleado.exception.LegajoInvalidoException;
import empleado.exception.SalarioInferiorAlMinimoException;
import empleado.exception.SalarioInvalidoException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
import java.util.regex.Pattern;

public class Empleado {

    // 3 letras mayusculas seguidas de 4 numeros. Ej: ABC1234
    private static final Pattern FORMATO_LEGAJO = Pattern.compile("^[A-Z]{3}[0-9]{4}$");

    private final UUID id;
    private final String nombreCompleto;
    private final String legajo;
    private final LocalDate fechaIngreso;
    private final Puesto puesto;
    private final BigDecimal salario;

    private Empleado(UUID id, String nombreCompleto, String legajo,
                     LocalDate fechaIngreso, Puesto puesto, BigDecimal salario) {
        this.id = id;
        this.nombreCompleto = nombreCompleto;
        this.legajo = legajo;
        this.fechaIngreso = fechaIngreso;
        this.puesto = puesto;
        this.salario = salario;
    }

    public static Empleado instancia(UUID id, String nombreCompleto, String legajo,
                                     LocalDate fechaIngreso, Puesto puesto, BigDecimal salario) {
        validarObligatorios(id, nombreCompleto, legajo, fechaIngreso, puesto, salario);
        validarFechaIngreso(fechaIngreso);
        validarLegajo(legajo);
        validarSalario(puesto, salario);

        return new Empleado(id, nombreCompleto, legajo, fechaIngreso, puesto, salario);
    }

    private static void validarObligatorios(UUID id, String nombreCompleto, String legajo,
                                            LocalDate fechaIngreso, Puesto puesto, BigDecimal salario) {
        if (id == null) {
            throw new EmpleadoIncompletoException("El id del empleado es obligatorio");
        }
        if (nombreCompleto == null || nombreCompleto.isBlank()) {
            throw new EmpleadoIncompletoException("El nombre completo del empleado es obligatorio");
        }
        if (legajo == null || legajo.isBlank()) {
            throw new EmpleadoIncompletoException("El legajo del empleado es obligatorio");
        }
        if (fechaIngreso == null) {
            throw new EmpleadoIncompletoException("La fecha de ingreso del empleado es obligatoria");
        }
        if (puesto == null) {
            throw new EmpleadoIncompletoException("El puesto del empleado es obligatorio");
        }
        if (salario == null) {
            throw new EmpleadoIncompletoException("El salario del empleado es obligatorio");
        }
    }

    private static void validarFechaIngreso(LocalDate fechaIngreso) {
        if (fechaIngreso.isAfter(LocalDate.now())) {
            throw new FechaIngresoInvalidaException("La fecha de ingreso no puede ser posterior a la fecha actual");
        }
    }

    private static void validarLegajo(String legajo) {
        if (!FORMATO_LEGAJO.matcher(legajo).matches()) {
            throw new LegajoInvalidoException(
                    "El legajo debe tener 3 letras mayusculas seguidas de 4 numeros (ej. ABC1234)");
        }
    }

    private static void validarSalario(Puesto puesto, BigDecimal salario) {
        // Primero > 0 y despues el piso: un salario 0 tambien esta bajo el piso,
        // pero el motivo correcto es que no es positivo.
        if (salario.compareTo(BigDecimal.ZERO) <= 0) {
            throw new SalarioInvalidoException("El salario debe ser mayor a cero");
        }
        if (!puesto.admiteSalario(salario)) {
            throw new SalarioInferiorAlMinimoException(
                    "El salario minimo para " + puesto + " es " + puesto.getSalarioMinimo());
        }
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
