package com.techlab.ecommerce.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.techlab.ecommerce.model.Cliente;

//la interfaz vacia va a heredar metodos save, findById, deleteById, etc

public interface ClienteRepository extends JpaRepository<Cliente, Integer> {
    
}
