package ec.edu.ups.icc.proyectointegrador.common.exception.base;

public abstract class ApplicationException extends RuntimeException {
    
    public ApplicationException(String message) {
        super(message);
    }
}
