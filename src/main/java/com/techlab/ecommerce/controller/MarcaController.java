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

import com.techlab.ecommerce.model.Marca;
import com.techlab.ecommerce.service.MarcaService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/marcas") 
@RequiredArgsConstructor
// @CrossOrigin(origins = "http://127.0.0.1:5500") 
public class MarcaController {
    private final MarcaService marcaService;

    @GetMapping
    public ResponseEntity <List<Marca>> listarTodas(){
        return ResponseEntity.ok( marcaService.listarTodas());
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerMarca(@PathVariable int id){
         Marca marca = marcaService.obtenerPorId(id);
            return ResponseEntity.ok(marca);
    }
     @PostMapping
    public ResponseEntity<Marca> crearMarca( @Valid @RequestBody Marca nuevaMarca) { 
            Marca creada = marcaService.guardar( nuevaMarca );
            return ResponseEntity.status(HttpStatus.CREATED).body(creada);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar( @PathVariable int id,  @Valid @RequestBody Marca datos){
            Marca actualizada= marcaService.actualizar( id, datos);
            return ResponseEntity.ok(actualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarMarca(@PathVariable int id) {
            marcaService.eliminar(id);
            return ResponseEntity.ok().build();

    }}

        
