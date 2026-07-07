package com.techlab.ecommerce.controller;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.techlab.ecommerce.model.Cliente;
import com.techlab.ecommerce.service.ClienteService;

import jakarta.validation.Valid; 

    @RestController //indica el endpoint
    @RequestMapping("/clientes")
    // @CrossOrigin(origins = "*") 
        public class ClienteController {
            private final ClienteService  service;

        public ClienteController (ClienteService service ){
            this.service= service;
        }
    @GetMapping
        public List<Cliente> listarTodos(){
            return service.listarTodos();
    }
    @GetMapping("/{DNI}")
        public Cliente obtenerCliente (@PathVariable int DNI){
            return service.obtenerPorDni(DNI);
        }
    @PostMapping("")
        public ResponseEntity<?> crearCliente( @Valid @RequestBody Cliente nuevoCliente){
            Cliente creado= service.guardar(nuevoCliente);
            return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @PutMapping("/{DNI}")
         public ResponseEntity<?> actualizar( @PathVariable int DNI, @Valid @RequestBody Cliente datos){
                Cliente actualizado= service.actualizar(DNI, datos);
                return ResponseEntity.ok(actualizado);
    }
        
    @DeleteMapping("/{DNI}")
        public ResponseEntity<?> eliminarCliente( @PathVariable int DNI ){
                service.eliminar(DNI);
                System.out.println("El cliente fue eliminado");
                return ResponseEntity.ok().build();
        }}
