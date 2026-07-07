package com.techlab.ecommerce.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.techlab.ecommerce.model.Categoria;
import com.techlab.ecommerce.service.CategoriaService;

@RestController
@SuppressWarnings("unused")
@RequestMapping("/categorias")
//    @CrossOrigin(origins = "http://127.0.0.1:5500") 
public class CategoriaController {  
    
    private final CategoriaService service;

    public CategoriaController (CategoriaService service){
        this.service = service;
    }

    @GetMapping

    public ResponseEntity<Categoria[]> obtenerCategoriasDisponibles() {
        return ResponseEntity.ok(Categoria.values()); // Devuelve la lista de enums como un array JSON
    }
    }

