package com.techlab.ecommerce.exception;


public class MarcaNoEncontradaException extends RuntimeException{
    public MarcaNoEncontradaException (String mensaje){
        super(mensaje);
}
}