package ar.edu.utn.frbb.tup.model.exception;

public class NoAlcanzaException extends Throwable {
    public NoAlcanzaException() {
        super();
    }
    public NoAlcanzaException(String mensaje){
        super(mensaje);
    }
}
