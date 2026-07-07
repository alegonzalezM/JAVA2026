package com.techlab.ecommerce.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.techlab.ecommerce.model.Carrito;
import com.techlab.ecommerce.service.CarritoService;

@RestController
@RequestMapping("/carritos")
public class CarritoController {

    private final CarritoService service;

    public CarritoController(CarritoService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Carrito>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Carrito> obtenerCarrito(@PathVariable Integer id) {
        return ResponseEntity.ok(service.obtenerPorId(id));
    }

    // Crea un carrito vacío — debe existir antes de agregar productos
    @PostMapping
    public ResponseEntity<Carrito> crear() {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear());
    }

    // Dos @PathVariable porque necesita identificar el carrito y el producto.
    // No usa @RequestBody — toda la información está en la URL.
    @PostMapping("/{carritoId}/productos/{productoId}")
    public ResponseEntity<Carrito> agregarProducto(
            @PathVariable Integer carritoId,
            @PathVariable Integer productoId) {
        return ResponseEntity.ok(service.agregarProducto(carritoId, productoId));
    }

    // Vacía el carrito sin eliminarlo — el usuario puede seguir usándolo
    @DeleteMapping("/{id}/vaciar")
    public ResponseEntity<Carrito> vaciar(@PathVariable Integer id) {
        return ResponseEntity.ok(service.vaciar(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        service.eliminar(id);
        return ResponseEntity.ok().build();
    }
}