package ec.edu.ups.icc.proyectointegrador.common.exception.domain;

import ec.edu.ups.icc.proyectointegrador.common.exception.base.ApplicationException;

public class ForbiddenOperationException extends ApplicationException {
    
    public ForbiddenOperationException(String message) {
        super(message);
    }
}
