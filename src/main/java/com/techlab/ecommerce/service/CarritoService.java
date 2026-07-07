package com.techlab.ecommerce.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.techlab.ecommerce.exception.CarritoNoEncontradoException;
import com.techlab.ecommerce.exception.StockInsuficienteException;
import com.techlab.ecommerce.model.Carrito;
import com.techlab.ecommerce.model.CarritoProducto;
import com.techlab.ecommerce.model.Producto;
import com.techlab.ecommerce.repository.CarritoProductoRepository;
import com.techlab.ecommerce.repository.CarritoRepository;
;

@Service
public class CarritoService {

    private final CarritoRepository carritoRepository;
    private final ProductoService productoService;
    private final CarritoProductoRepository carritoProductoRepository;

    public CarritoService(CarritoRepository carritoRepository,
                          CarritoProductoRepository carritoProductoRepository, ProductoService productoService) {
        this.carritoRepository = carritoRepository;
        this.productoService = productoService;
        this.carritoProductoRepository = carritoProductoRepository;                 
    }

    public Carrito crear() {
        return carritoRepository.save(new Carrito());
    }
// Cambia la anotación eliminando el (readOnly = true)
@Transactional 
public Carrito obtenerPorId(Integer id){ 
    Carrito carrito = carritoRepository.findById(id)
                            .orElseThrow(() -> new CarritoNoEncontradoException(
                                "No se encontró un carrito con id " + id));
    
    if (carrito.getProductos() != null) {
        carrito.getProductos().size(); 
    }
    return carrito;
}

    public List<Carrito> listarTodos() {
        return carritoRepository.findAll();
    }

    public Carrito agregarProducto(Integer carritoId, Integer productoId) {
        Carrito carrito = obtenerPorId(carritoId);
        Producto producto = productoService.obtenerPorId(productoId);

        if (producto.getStock() <= 0) {
            throw new StockInsuficienteException(
                    "El producto \"" + producto.getNombre() + "\" no tiene stock disponible.");
        }

        Optional<CarritoProducto> existente = carritoProductoRepository.findByCarritoAndProducto(carrito, producto);

        if(existente.isPresent()){
            // El producto ya esta en el carrito incrementamos 
            CarritoProducto cp = existente.get();
            cp.setCantidad(cp.getCantidad() + 1);
            carritoProductoRepository.save(cp);

        }else{
            // el producto no esta en el carrito - creamos una fila nueva
            CarritoProducto nuevo = new CarritoProducto(null,carrito,producto,1);
            carritoProductoRepository.save(nuevo);
        }

        // Descuenta una unidad de stock y persiste el cambio
        producto.setStock(producto.getStock() - 1);
        productoService.guardar(producto);

        return carritoRepository.save(carrito);
    }

    // clear() quita los productos de la lista en memoria.
    // save() persiste ese cambio eliminando las filas de la tabla intermedia.
    public Carrito vaciar(Integer id) {
        Carrito carrito = obtenerPorId(id);
        carrito.getProductos().clear();
        return carritoRepository.save(carrito);
    }

    public void eliminar(Integer id) {
        Carrito carrito = obtenerPorId(id);
        carritoRepository.delete(carrito);
    }
}