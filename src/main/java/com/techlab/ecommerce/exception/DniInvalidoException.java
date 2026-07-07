package com.techlab.ecommerce.exception;

public class DniInvalidoException extends RuntimeException{
    public DniInvalidoException (String mensaje){
        super(mensaje);
}
}

