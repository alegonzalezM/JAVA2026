package com.techlab.ecommerce.exception;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ProductoNoEncontradoException.class)
    public ResponseEntity<String> manejarTituloInvalido(ProductoNoEncontradoException e){
        return ResponseEntity.badRequest().body(e.getMessage());
    }
    @ExceptionHandler(ClienteNoEncontradoException.class)
    public ResponseEntity<String> manejarClienteNoEncontrado(ClienteNoEncontradoException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
    @ExceptionHandler(CodigoIncorrectoException.class)
    public ResponseEntity<String> manejarCodigoIncorrecto(CodigoIncorrectoException e){
        return ResponseEntity.badRequest().body(e.getMessage());
    }
    @ExceptionHandler(MarcaNoEncontradaException.class)
    public ResponseEntity<String> manejarMarcaNOEncontrada(MarcaNoEncontradaException e){
        return ResponseEntity.status(400).body(e.getMessage());
    }
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> manejarIllegalArgumentException(IllegalArgumentException e) {
    // Si el mensaje del error contiene la ruta de un Enum de Categoría
    if (e.getMessage() != null && e.getMessage().contains("com.techlab.ecommerce.model.Categoria")) {
        return ResponseEntity.badRequest().body("La categoría ingresada no existe en el sistema.");
    }
    return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler( ArgumentoNoValidoException.class)
    public ResponseEntity<String> manejarValidacionesCampos(ArgumentoNoValidoException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
    @ExceptionHandler( StockInsuficienteException.class)
    public ResponseEntity<String> manejarStockInsuficiente(StockInsuficienteException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
    @ExceptionHandler( CarritoNoEncontradoException.class)
    public ResponseEntity<String> manejarCarritoNoEncontrado(CarritoNoEncontradoException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
    @ExceptionHandler( MethodArgumentNotValidException.class) //uso map por si hay + de 1 error p' mostrar
    public ResponseEntity<Map<String, String>> manejarValidacion(
        MethodArgumentNotValidException e) {  Map<String, String> errores = new HashMap<>();
        for(FieldError error : e.getBindingResult().getFieldErrors())
        errores.put(error.getField(), error.getDefaultMessage());
                 return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errores);
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> manejarErroresGlobales(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body("Ocurrió un error inesperado en el servidor: " + e.getMessage());
    }

   
    }
    
