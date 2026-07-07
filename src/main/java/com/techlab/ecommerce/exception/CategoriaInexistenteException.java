package com.techlab.ecommerce.exception;

public class CategoriaInexistenteException extends RuntimeException{
    public CategoriaInexistenteException (String mensaje){
        super(mensaje);
}
}

