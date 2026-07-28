package ec.edu.ups.icc.proyectointegrador.common.exception.domain;

import ec.edu.ups.icc.proyectointegrador.common.exception.base.ApplicationException;

public class ConflictException extends ApplicationException {
    public ConflictException(String message) {
        super(message);
    }
}
