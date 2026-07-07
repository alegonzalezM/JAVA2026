package com.techlab.ecommerce.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.techlab.ecommerce.model.Categoria;
import com.techlab.ecommerce.model.Marca;
import com.techlab.ecommerce.model.Producto;

//la interfaz vacia va a heredar metodos save, findById, deleteById, etc

//Filtra por el campo nombre con JPQL(el codigo lo creo en forma manual y el nombre del método también). Requiere
//@Param("nombre") p' enlazar el parámetro
public interface ProductoRepository extends JpaRepository<Producto, Integer> {
    @Query ("SELECT p FROM Producto p WHERE p.nombre LIKE %:nombre%")  //consultas personalizadas
    List<Producto> obtenerPorNombre(@Param("nombre") String nombre);

    //Filtra por categoria, se genera automáticamente implementando el método de Spring JPA findBy
    List <Producto> findByCategoria(Categoria categoria);
    List<Producto> findByPrecioLessThan(double precio); //otro metodo findBy
    List<Producto> findByMarca( Marca marca);
    
    @Query ("SELECT p FROM Producto p WHERE p.marca.nombre LIKE %:marca%")  //consultas personalizadas
    List<Producto> obtenerPorMarca(@Param("marca") String marca);
} 