package ec.edu.ups.icc.proyectointegrador.common.exception.domain;

import ec.edu.ups.icc.proyectointegrador.common.exception.base.ApplicationException;

public class TooManyRequestsException extends ApplicationException {
    public TooManyRequestsException(String message) {
        super(message);
    }
    
}
