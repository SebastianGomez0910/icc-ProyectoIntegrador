package ec.edu.ups.icc.proyectointegrador.common.exception.domain;

import ec.edu.ups.icc.proyectointegrador.common.exception.base.ApplicationException;

public class ResourceNotFoundException extends ApplicationException {
    
    public ResourceNotFoundException(String message) {
        super(message);
    }
}

