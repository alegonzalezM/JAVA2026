package com.techlab.ecommerce.service;
import java.util.List;
import org.springframework.stereotype.Service;
import com.techlab.ecommerce.exception.MarcaNoEncontradaException;
import com.techlab.ecommerce.exception.ProductoNoEncontradoException;
import com.techlab.ecommerce.model.Categoria;
import com.techlab.ecommerce.model.Marca;
import com.techlab.ecommerce.model.Producto;
import com.techlab.ecommerce.repository.MarcaRepository;
import com.techlab.ecommerce.repository.ProductoRepository;

@Service
public class ProductoService {

    private final ProductoRepository repository; //Inyeccion por constructor, recibe el repositorio
    private final MarcaRepository marcaRepository; //Inyeccion por constructor, recibe el repositorio<

    public ProductoService(ProductoRepository repository, MarcaRepository marcaRepository){
        this.repository = repository;
        this.marcaRepository= marcaRepository;
    }
    public Producto guardar(Producto p) {
        // if (p.getMarca() == null || p.getMarca().getId() == 0) {
        //     throw new MarcaNoEncontradaException("Debes especificar una marca válida para el producto.");
        // }
        int idMarca = p.getMarca().getId();
        Marca marcaReal = marcaRepository.findById(idMarca)
            .orElseThrow(() -> new com.techlab.ecommerce.exception.MarcaNoEncontradaException("La marca con ID " + idMarca + " no existe."));
        p.setMarca(marcaReal);

        return repository.save(p);
    }
 
    public List<Producto> listarTodos() {
        return repository.findAll();
    }
    public Producto obtenerPorId(Integer id) {
                return repository.findById(id)
            .orElseThrow(() -> new ProductoNoEncontradoException("No se encontro un producto con id " + id));
    }
    
    public List<Producto> obtenerPorNombre(String nombre) { //BUSCAR con sentencia JPQL en ProductoRepository
        List<Producto> productos = repository.obtenerPorNombre(nombre);
            if (productos.isEmpty()) {
                throw new ProductoNoEncontradoException("No se encontraron productos con el nombre: " + nombre);
    } 
            return productos;
    }
    public List<Producto> findByCategoria(Categoria categoria){
        List<Producto> productos = repository.findByCategoria(categoria); //BUSCAR con metodo findBy
            if (productos.isEmpty()) {
                throw new ProductoNoEncontradoException("No se encontraron productos en la categoría: " + categoria);
    }
            return productos;
    }
    public List<Producto> findByPrecioLessThan(double precio){ //nombrar siempre en ingles si es metodo de JPA
        List<Producto> productos= repository.findByPrecioLessThan(precio);
            if(productos.isEmpty()){
                throw new ProductoNoEncontradoException("No hay productos en ese rango de precios");
            }
            return productos;
    }
     public List<Producto> findByMarca(Marca marca){ 
        List<Producto> productos = repository.findByMarca(marca);
        if(productos.isEmpty()){
            throw new ProductoNoEncontradoException("No existe la marca buscada");
        }
        return productos;
    }
    
     public Producto actualizar(Integer id, Producto datos) {
        if (datos.getMarca() == null) {
            throw new IllegalArgumentException("La marca del pro es obligatoria");
        }
        Producto p = obtenerPorId(id);
        int idMarca = datos.getMarca().getId();
        Marca marcaReal = marcaRepository.findById(idMarca)
            .orElseThrow(() -> new MarcaNoEncontradaException("La marca con ID " + idMarca + " no existe."));
          
        p.setCodigo(datos.getCodigo());
        p.setNombre(datos.getNombre());
        p.setPrecio(datos.getPrecio());
        p.setStock(datos.getStock());
        p.setCategoria(datos.getCategoria());
        p.setMarca(marcaReal); 
        if(datos.getImagen()!=null){
            p.setImagen(datos.getImagen());}

        return repository.save(p);         
    }

    public void eliminar(Integer id) {
        Producto p = obtenerPorId(id);
        repository.delete(p);
    }
}