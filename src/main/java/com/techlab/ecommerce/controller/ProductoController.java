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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.techlab.ecommerce.model.Categoria;
import com.techlab.ecommerce.model.Marca;
import com.techlab.ecommerce.model.Producto;
import com.techlab.ecommerce.service.MarcaService;
import com.techlab.ecommerce.service.ProductoService; 

import jakarta.validation.Valid;
    
@RestController
@RequestMapping("/productos")
// @CrossOrigin(origins = "http://127.0.0.1:5500")
public class ProductoController {

    private final ProductoService service; //inyeccion por constructor. No se crea con new, Spring lo crea automatica/ y se lo pasa al controlador
    private final MarcaService marcaService;

    public ProductoController (ProductoService service, MarcaService marcaService){
        this.service= service;
        this.marcaService = marcaService;
    }

    @GetMapping
    public  ResponseEntity<List <Producto>> listarTodos(){
        return ResponseEntity.ok(service.listarTodos());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Producto> obtenerProducto(@PathVariable Integer id) {
            return ResponseEntity.ok(service.obtenerPorId(id));
       }

@GetMapping("/nombre/{nombre}")
public ResponseEntity<List<Producto>> buscarPorNombre(@PathVariable String nombre){
    return ResponseEntity.ok(service.obtenerPorNombre(nombre));
}
    @GetMapping("/categorias/{categoria}")
public ResponseEntity<List<Producto>> buscarPorCategoria(@PathVariable Categoria categoria){
    return ResponseEntity.ok(service.findByCategoria(categoria));
}


    @GetMapping("/buscar") //diferentes busquedas
    public ResponseEntity<?> buscarProductos(
        @RequestParam(required= false) String nombre,
        @RequestParam(required= false) String categoria, 
        @RequestParam(required = false) Double precioMax,
        @RequestParam(required= false) Integer marcaId) {

            if (nombre != null && !nombre.isBlank()) {  //nombre, usa la consulta JPQL personalizada
                List<Producto> porNombre = service.obtenerPorNombre(nombre);
                return ResponseEntity.ok(porNombre);
            }
            if (categoria != null && !categoria.isBlank()) { //  Filtro por Categoría con metodo findBy automatico de JPA
                    Categoria categoriaEnum = Categoria.valueOf(categoria.trim().toUpperCase());
                    List<Producto> porCategoria = service.findByCategoria(categoriaEnum);
                    return ResponseEntity.ok(porCategoria);
                }
            
            if (precioMax != null) { // busqueda por precio menor que con query de JPA
                    List<Producto> porPrecio = service.findByPrecioLessThan(precioMax); 
                    return ResponseEntity.ok(porPrecio);
                }
            if (marcaId != null){
                    Marca marcaReal = marcaService.obtenerPorId(marcaId);
                    List<Producto> listaMarca = service.findByMarca(marcaReal); 
                    return ResponseEntity.ok(listaMarca);
            }
            return ResponseEntity.badRequest().body("Debes ingresar al menos un parámetro de búsqueda: 'nombre', 'categoria', 'marca' o 'precio' según el caso" );

        }
        
    @PostMapping
    public ResponseEntity<?> crearProducto( @Valid @RequestBody Producto nuevoProducto) { 
            if (nuevoProducto.getCategoria() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("""
                    La categoría ingresada no es válida o está mal escrita. Utilice:  \r
                        BICICLETAS,\r
                        REPUESTOS,\r
                        ACCESORIOS,\r
                        HERRAMIENTAS,\r
                        INDUMENTARIA,\r
                        VARIOS;""");
            }
            Producto creado = service.guardar(nuevoProducto);
            return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Producto> actualizar( @PathVariable Integer id,  @Valid @RequestBody Producto datos){
          return ResponseEntity.ok(service.actualizar(id, datos));
          }
        
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminaProducto(@PathVariable Integer id){
            service.eliminar(id);
            return ResponseEntity.ok().build();
        }
    }

