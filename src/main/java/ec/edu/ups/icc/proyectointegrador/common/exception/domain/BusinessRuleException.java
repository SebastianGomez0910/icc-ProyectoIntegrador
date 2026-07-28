package ec.edu.ups.icc.proyectointegrador.common.exception.domain;

import ec.edu.ups.icc.proyectointegrador.common.exception.base.ApplicationException;

public class BusinessRuleException extends ApplicationException {
    
    public BusinessRuleException(String message) {
        super(message);
    }
}

