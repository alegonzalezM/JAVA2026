package com.techlab.ecommerce.repository;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.techlab.ecommerce.model.Carrito;
import com.techlab.ecommerce.model.CarritoProducto;
import com.techlab.ecommerce.model.Producto;


public interface CarritoProductoRepository extends JpaRepository<CarritoProducto, Integer> {
    
    Optional<CarritoProducto> findByCarritoAndProducto(Carrito carrito, Producto producto);
       List<CarritoProducto> findByCarrito(Carrito carrito);
}
