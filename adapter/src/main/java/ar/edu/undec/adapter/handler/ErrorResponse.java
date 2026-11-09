package ar.edu.undec.adapter.handler;

/*
 * Formato comun de todas las respuestas de error: {"mensaje": "..."}
 */
public class ErrorResponse {

    private final String mensaje;

    public ErrorResponse(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getMensaje() {
        return mensaje;
    }
}
