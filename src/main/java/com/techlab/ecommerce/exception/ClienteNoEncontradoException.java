package com.techlab.ecommerce.exception;

public class ClienteNoEncontradoException extends RuntimeException{
    public ClienteNoEncontradoException(String mensaje){
        super(mensaje);
    }
    
}

