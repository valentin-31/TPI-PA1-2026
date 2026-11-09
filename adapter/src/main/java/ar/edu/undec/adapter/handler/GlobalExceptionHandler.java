package ar.edu.undec.adapter.handler;

import empleado.exception.EmpleadoExisteException;
import empleado.exception.EmpleadoIncompletoException;
import empleado.exception.EmpleadoNoEncontradoException;
import empleado.exception.FechaIngresoInvalidaException;
import empleado.exception.LegajoInvalidoException;
import empleado.exception.SalarioInferiorAlMinimoException;
import empleado.exception.SalarioInvalidoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/*
 * Manejador global: atrapa las excepciones que salen de CUALQUIER controller
 * y las traduce al codigo HTTP que corresponde.
 *
 *   Reglas del dominio violadas  -> 400 Bad Request
 *   JSON que no se puede leer    -> 400 Bad Request
 *   Empleado no encontrado       -> 404 Not Found
 *   Legajo duplicado             -> 409 Conflict
 *   Errores propios de Spring    -> su propio codigo (415, 405, 404 de ruta inexistente...)
 *   Cualquier otro error         -> 500 Internal Server Error, con mensaje generico
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // 400: el pedido trae datos que violan las reglas del dominio
    @ExceptionHandler({
            EmpleadoIncompletoException.class,
            FechaIngresoInvalidaException.class,
            LegajoInvalidoException.class,
            SalarioInvalidoException.class,
            SalarioInferiorAlMinimoException.class
    })
    public ResponseEntity<ErrorResponse> manejarReglaDeDominioViolada(RuntimeException excepcion) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(excepcion.getMessage()));
    }

    // 400: el JSON esta mal formado o tiene un valor imposible de convertir (ej. un puesto que no existe)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> manejarJsonIlegible(HttpMessageNotReadableException excepcion) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(
                "El cuerpo del pedido no es valido. Revise el formato de los datos "
                        + "(fecha yyyy-MM-dd, puesto ANALISTA, SUPERVISOR o GERENTE)"));
    }

    // 404: se busco un recurso que no existe
    @ExceptionHandler(EmpleadoNoEncontradoException.class)
    public ResponseEntity<ErrorResponse> manejarNoEncontrado(EmpleadoNoEncontradoException excepcion) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(excepcion.getMessage()));
    }

    // 409: el recurso choca con uno que ya existe
    @ExceptionHandler(EmpleadoExisteException.class)
    public ResponseEntity<ErrorResponse> manejarDuplicado(EmpleadoExisteException excepcion) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(excepcion.getMessage()));
    }

    // Red de seguridad: atiende cualquier excepcion que no haya atendido un metodo mas especifico
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> manejarErrorNoPrevisto(Exception excepcion) {
        // Errores propios de Spring (415, 405, 404 de ruta inexistente...): ya traen su codigo correcto.
        // Se respeta ese codigo y solo se le da el formato {"mensaje"}.
        if (excepcion instanceof org.springframework.web.ErrorResponse errorDeSpring) {
            String detalle = errorDeSpring.getBody().getDetail();
            String mensaje = (detalle != null) ? detalle : "No se pudo procesar el pedido";
            return ResponseEntity.status(errorDeSpring.getStatusCode()).body(new ErrorResponse(mensaje));
        }

        // Error inesperado de verdad: el detalle queda en el log del servidor
        // y al cliente se le responde un mensaje generico, sin exponer SQL ni datos internos.
        log.error("Error no previsto al procesar el pedido", excepcion);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("Error interno del servidor"));
    }
}
