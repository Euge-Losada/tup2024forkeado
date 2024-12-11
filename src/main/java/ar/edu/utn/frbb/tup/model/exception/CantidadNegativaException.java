package ar.edu.utn.frbb.tup.model.exception;

public class CantidadNegativaException extends Throwable {
    public CantidadNegativaException() {
        super();
    }

    public CantidadNegativaException(String mensaje) {
        super(mensaje);
    }
}
