package com.techlab.ecommerce.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.techlab.ecommerce.model.Marca;


public interface MarcaRepository extends JpaRepository<Marca, Integer> {
  boolean existsByNombre(String nombre); // lo uso en actualizar() de MarcaService(Marca no admite nombres duplicados)
}
